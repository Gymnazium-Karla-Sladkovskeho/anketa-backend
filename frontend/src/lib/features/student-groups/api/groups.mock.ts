import type { StudentListInput } from '$lib/core/api/contracts';
import type { MockContext } from '$lib/core/api/mocks/context';
import type { MockHandler } from '$lib/core/api/mocks/server';
import { body, fail, id, object, respond } from '$lib/core/api/mocks/helpers';

export function createGroupsMock(context: MockContext): MockHandler {
    let nextId = 2;
    const groups: { id: number; owner: string; name?: string; students: number[] }[] = [
        { id: 1, owner: 'teacher-1', name: 'Demo: skupina', students: [1, 2] }
    ];
    context.onAccountRemoved(account => {
        for (let i = groups.length - 1; i >= 0; i--) {
            if (groups[i].owner === account.key) groups.splice(i, 1);
            else if (account.role === 'student') groups[i].students = groups[i].students.filter(value => value !== account.entityId);
        }
    });
    async function input(request: Request): Promise<StudentListInput> {
        const value = await body(request);
        if (value.name !== undefined && typeof value.name !== 'string') fail(400, 'Invalid name');
        if (!Array.isArray(value.students)) fail(400, 'Expected students array');
        const students = value.students.map(student => ({ id: id(object(student).id) }));
        if (students.some(student => !context.accounts.some(a => a.role === 'student' && a.entityId === student.id))) fail(400, 'Unknown student');
        return { name: value.name as string | undefined, students };
    }
    return request => {
        const match = /^\/student-list(?:\/(\d+))?$/.exec(new URL(request.url).pathname);
        if (!match) return;
        return respond(async () => {
            const user = context.requireUser(request, ['teacher', 'admin']);
            const entityId = match[1] ? id(match[1]) : undefined;
            const visible = (group: typeof groups[number]) => group.owner === user.key || user.role === 'admin';
            if (!entityId && request.method === 'GET') return groups.filter(visible).map(({ id, name }) => ({ id, name }));
            if (!entityId && request.method === 'POST') {
                const value = await input(request);
                groups.push({ id: nextId++, owner: user.key, name: value.name, students: [...new Set(value.students.map(s => s.id))] });
                return;
            }
            const group = groups.find(g => g.id === entityId);
            if (!group) fail(404, 'Not found');
            if (!visible(group)) fail(403, 'Forbidden');
            if (request.method === 'GET') return {
                id: group.id, name: group.name,
                students: group.students.map(studentId => {
                    const account = context.accounts.find(a => a.role === 'student' && a.entityId === studentId)!;
                    return { id: studentId, name: account.name, lastname: account.lastname };
                })
            };
            if (request.method === 'PUT') {
                const value = await input(request);
                group.name = value.name;
                group.students = [...new Set(value.students.map(s => s.id))];
                return;
            }
            if (request.method === 'DELETE') { groups.splice(groups.indexOf(group), 1); return; }
            fail(405, 'Method not allowed');
        }, request.method === 'POST' ? 201 : ['PUT', 'DELETE'].includes(request.method) ? 204 : 200);
    };
}
