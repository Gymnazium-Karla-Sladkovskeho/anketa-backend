import type { MockContext } from '$lib/core/api/mocks/context';
import type { MockHandler } from '$lib/core/api/mocks/server';
import { body, fail, id, pagination, respond, slicePage, text } from '$lib/core/api/mocks/helpers';

export function createUsersMock(context: MockContext): MockHandler {
    const classes = new Map([[1, '1.A'], [2, '2.B']]);
    let nextStudent = 3;
    let nextTeacher = 3;
    return request => {
        const url = new URL(request.url);
        const match = /^\/(student|teacher)(?:\/(\d+))?$/.exec(url.pathname);
        if (!match) return;
        return respond(async () => {
            context.requireUser(request, ['admin']);
            const type = match[1] as 'student' | 'teacher';
            const entityId = match[2] ? id(match[2]) : undefined;
            const toDto = (account: (typeof context.accounts)[number]) => ({
                id: account.entityId!, name: account.name, lastname: account.lastname,
                ...(type === 'teacher' ? { active: account.active } : { class: classes.get(account.entityId!) })
            });
            const all = context.accounts.filter(a => a.role === type);
            if (!entityId && request.method === 'GET') {
                if (type === 'teacher') {
                    const { page, pageSize } = pagination(url);
                    return slicePage(all.map(toDto), page, pageSize);
                }
                if (!url.searchParams.has('classes')) fail(400, 'classes query parameter is required');
                const selected = url.searchParams.getAll('classes').flatMap(value => value.split(',')).filter(Boolean);
                return all.filter(a => selected.includes(classes.get(a.entityId!) ?? '')).map(toDto);
            }
            if (!entityId && request.method === 'POST') {
                const input = await body(request);
                const name = text(input.name), lastname = text(input.lastname);
                if (input.class !== undefined && typeof input.class !== 'string') fail(400, 'Invalid class');
                const newId = type === 'teacher' ? nextTeacher++ : nextStudent++;
                const account = { key: `${type}-${newId}`, username: `${type}${newId}`, password: `demo-${crypto.randomUUID().slice(0, 8)}`, name, lastname, role: type, entityId: newId, active: true };
                context.accounts.push(account);
                if (type === 'student' && typeof input.class === 'string') classes.set(newId, input.class);
                return { username: account.username, password: account.password };
            }
            const account = all.find(a => a.entityId === entityId);
            if (!account) fail(404, 'Not found');
            if (request.method === 'GET') return toDto(account);
            if (request.method === 'PUT') {
                const input = await body(request);
                const name = text(input.name), lastname = text(input.lastname);
                if (type === 'teacher' && typeof input.active !== 'boolean') fail(400, 'Expected active boolean');
                if (type === 'student' && input.class !== undefined && typeof input.class !== 'string') fail(400, 'Invalid class');
                Object.assign(account, { name, lastname });
                if (type === 'teacher') {
                    account.active = input.active as boolean;
                    if (!account.active) context.revoke(account.key);
                } else if (typeof input.class === 'string') classes.set(entityId!, input.class);
                else classes.delete(entityId!);
                context.accountUpdated(account);
                return;
            }
            if (request.method === 'DELETE') {
                context.removeAccount(account);
                if (type === 'student') classes.delete(entityId!);
                return;
            }
            fail(405, 'Method not allowed');
        }, request.method === 'POST' ? 201 : ['PUT', 'DELETE'].includes(request.method) ? 204 : 200);
    };
}
