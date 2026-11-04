import type { MockContext } from '$lib/core/api/mocks/context';
import type { MockHandler } from '$lib/core/api/mocks/server';
import { body, fail, id, respond, text } from '$lib/core/api/mocks/helpers';

export function createAuthMock(context: MockContext): MockHandler {
    return request => {
        const path = new URL(request.url).pathname;
        if (path === '/auth' && request.method === 'POST') return respond(async () => {
            const input = await body(request);
            const account = context.accounts.find(a => a.username === input.username && a.password === input.password && a.active);
            if (!account) fail(404, 'Invalid credentials');
            return context.issue(account);
        }, 201);
        if (path === '/auth/me' && request.method === 'GET') return respond(() => {
            const user = context.requireUser(request);
            return { name: user.name, lastname: user.lastname, role: user.role === 'student' ? 'žák' : user.role === 'teacher' ? 'učitel' : 'admin' };
        });
        if (path === '/auth/password' && request.method === 'PATCH') return respond(async () => {
            const user = context.requireUser(request);
            user.password = text((await body(request)).password);
            // Minimum complexity rules are not specified. Do not invent them in mocks.
        }, 204);
        const match = /^\/auth\/password\/(\d+)$/.exec(path);
        if (match && request.method === 'POST') return respond(() => {
            context.requireUser(request, ['admin']);
            const type = new URL(request.url).searchParams.get('type');
            if (type !== 'student' && type !== 'teacher') fail(400, 'Expected student or teacher type');
            const account = context.accounts.find(a => a.role === type && a.entityId === id(match[1]));
            if (!account) fail(404, 'Not found');
            account.password = `demo-${crypto.randomUUID().slice(0, 8)}`;
            context.revoke(account.key);
            return { username: account.username, password: account.password };
        }, 201);
    };
}
