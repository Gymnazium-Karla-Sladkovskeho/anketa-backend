import type { Template } from '$lib/core/api/contracts';
import type { MockContext } from '$lib/core/api/mocks/context';
import type { MockHandler } from '$lib/core/api/mocks/server';
import { body, fail, id, respond } from '$lib/core/api/mocks/helpers';
import { templateInput } from '$lib/core/api/mocks/validation';

export function createTemplatesMock(context: MockContext): MockHandler {
    let nextId = 4;
    const records: { owner: string | null; template: Template }[] = [
        { owner: null, template: { id: 1, name: 'Demo: školní prostředí', questions: [{ type: 'OPEN', name: 'Co byste ve škole změnili?' }] } },
        { owner: 'teacher-1', template: { id: 2, name: 'Demo: zpětná vazba', questions: [{ type: 'CLOSE', name: 'Jak hodnotíte hodinu?', choices: [{ name: 'Dobře' }, { name: 'Špatně' }] }] } },
        { owner: 'teacher-2', template: { id: 3, name: 'Demo: druhý učitel', questions: [] } }
    ];
    context.onAccountRemoved(account => {
        for (let i = records.length - 1; i >= 0; i--) if (records[i].owner === account.key) records.splice(i, 1);
    });
    return request => {
        const url = new URL(request.url);
        const match = /^\/template(\/global)?(?:\/(\d+))?$/.exec(url.pathname);
        if (!match) return;
        return respond(async () => {
            const user = context.requireUser(request, ['teacher', 'admin']);
            const global = !!match[1];
            const entityId = match[2] ? id(match[2]) : undefined;
            if (global && request.method !== 'GET' && user.role !== 'admin') fail(403, 'Forbidden');
            if (!entityId && request.method === 'GET') {
                if (!global && !['true', 'false'].includes(url.searchParams.get('getAll') ?? '')) fail(400, 'Expected getAll boolean');
                return records.filter(record => global ? record.owner === null : record.owner === user.key || (url.searchParams.get('getAll') === 'true' && record.owner === null))
                    .map(({ template }) => ({ id: template.id, name: template.name }));
            }
            if (!entityId && request.method === 'POST') {
                const template = templateInput(await body(request));
                records.push({ owner: global ? null : user.key, template: { ...template, id: nextId++ } });
                return;
            }
            const record = records.find(r => r.template.id === entityId && (r.owner === null) === global);
            if (!record) fail(404, 'Not found');
            if (!global && record.owner !== user.key) fail(403, 'Forbidden');
            if (request.method === 'GET') return structuredClone(record.template);
            if (request.method === 'PUT') { record.template = { ...templateInput(await body(request)), id: entityId! }; return; }
            if (request.method === 'DELETE') { records.splice(records.indexOf(record), 1); return; }
            fail(405, 'Method not allowed');
        }, request.method === 'POST' ? 201 : ['PUT', 'DELETE'].includes(request.method) ? 204 : 200);
    };
}
