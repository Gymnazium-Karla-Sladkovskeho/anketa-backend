import type { Poll, PollInput, PollSummary, Teacher } from '$lib/core/api/contracts';
import type { MockContext } from '$lib/core/api/mocks/context';
import type { MockHandler } from '$lib/core/api/mocks/server';
import { body, fail, id, pagination, respond, slicePage, text } from '$lib/core/api/mocks/helpers';
import { questions } from '$lib/core/api/mocks/validation';

export function createSurveysMock(context: MockContext): MockHandler {
    let nextId = 5;
    const now = Date.now(), day = 86400_000;
    const date = (offset: number) => new Date(now + offset * day).toISOString();
    function teacher(key: string): Teacher {
        const account = context.accounts.find(a => a.key === key)!;
        return { id: account.entityId!, name: account.name, lastname: account.lastname, active: account.active };
    }
    const records: { owner: string; assigned: string[]; poll: Poll }[] = [
        { owner: 'teacher-1', assigned: ['student-1'], poll: { id: 1, name: 'Školní prostředí', from: date(-2), to: date(7), teacher: teacher('teacher-1'), questions: [
            { type: 'OPEN', name: 'Co byste ve škole změnili?' },
            { type: 'CLOSE', name: 'Jak se ve škole cítíte?', choices: [{ name: 'Velmi dobře' }, { name: 'Spíše dobře' }, { name: 'Spíše špatně' }, { name: 'Špatně' }] },
            { type: 'CLOSE', name: 'Které oblasti byste chtěli zlepšit?', choices: [{ name: 'Učebny' }, { name: 'Jídelna' }, { name: 'Odpočinkové prostory' }, { name: 'Školní akce' }] },
            { type: 'NUMERIC', name: 'Jak byste celkově ohodnotili školní prostředí?' }
        ] } },
        { owner: 'teacher-1', assigned: ['student-1'], poll: { id: 2, name: 'Demo: ukončená anketa', from: date(-14), to: date(-7), teacher: teacher('teacher-1'), questions: [] } },
        { owner: 'teacher-2', assigned: ['student-2'], poll: { id: 3, name: 'Demo: jiná skupina', from: date(-1), to: date(7), teacher: teacher('teacher-2'), questions: [] } },
        { owner: 'teacher-1', assigned: ['student-1'], poll: { id: 4, name: 'Demo: připravovaná anketa', from: date(7), to: date(14), teacher: teacher('teacher-1'), questions: [] } }
    ];
    context.onAccountUpdated(account => {
        for (const record of records) if (record.owner === account.key) record.poll.teacher = teacher(account.key);
    });
    context.onAccountRemoved(account => {
        for (let i = records.length - 1; i >= 0; i--) {
            if (records[i].owner === account.key) records.splice(i, 1);
            else records[i].assigned = records[i].assigned.filter(key => key !== account.key);
        }
    });
    const summary = ({ questions: _questions, ...poll }: Poll): PollSummary => structuredClone(poll);
    function input(value: Record<string, unknown>): PollInput {
        const name = text(value.name), from = text(value.from), to = text(value.to);
        if (!Number.isFinite(Date.parse(from)) || !Number.isFinite(Date.parse(to)) || Date.parse(from) >= Date.parse(to)) fail(400, 'Invalid date range');
        return { name, from, to, ...(value.questions === undefined ? {} : { questions: questions(value.questions) }) };
    }
    return request => {
        const url = new URL(request.url), path = url.pathname;
        if (!/^\/poll(?:\/|$)/.test(path)) return;
        return respond(async () => {
            const user = context.requireUser(request);
            const { method } = request;
            if (path === '/poll' && method === 'GET') {
                context.requireUser(request, ['teacher', 'admin']);
                if (url.searchParams.has('groups')) fail(501, 'groups filtering semantics need backend clarification');
                return records.filter(r => r.owner === user.key).map(r => summary(r.poll));
            }
            if (path === '/poll' && method === 'POST') {
                context.requireUser(request, ['teacher', 'admin']);
                // The spec has no admin teacher ID; use a synthetic author only within the mock.
                const author: Teacher = { id: user.entityId ?? 100, name: user.name, lastname: user.lastname, active: true };
                records.push({ owner: user.key, assigned: [], poll: { ...input(await body(request)), id: nextId++, teacher: author } });
                return;
            }
            if ((path === '/poll/global' || path === '/poll/respondent') && method === 'GET') {
                const global = path === '/poll/global';
                context.requireUser(request, global ? ['admin'] : ['student']);
                const { page, pageSize } = pagination(url);
                const authors = url.searchParams.getAll('id').flatMap(value => value.split(',')).map(id);
                const visible = records.filter(r => global ? !authors.length || authors.includes(r.poll.teacher.id) : r.assigned.includes(user.key));
                return { page, totalPages: Math.ceil(visible.length / pageSize), pageSize, payload: slicePage(visible.map(r => summary(r.poll)), page, pageSize) };
            }
            const match = /^\/poll\/(?:pdf\/)?(\d+)(\/response)?$/.exec(path);
            if (!match) fail(501, 'Mock endpoint not implemented');
            const record = records.find(r => r.poll.id === id(match[1]));
            if (!record) fail(404, 'Not found');
            const owns = record.owner === user.key;
            if (path.startsWith('/poll/pdf/') || match[2]) {
                if (method !== 'GET') fail(405, 'Method not allowed');
                if (!owns || user.role === 'student' || Date.parse(record.poll.to) > Date.now()) fail(403, 'Results are only available to the author after closure');
                if (path.startsWith('/poll/pdf/')) fail(501, 'PDF export has no agreed document specification');
                pagination(url);
                return []; // No fabricated or identifiable student responses.
            }
            if (method === 'GET') {
                if (!owns && user.role !== 'admin') {
                    if (user.role !== 'student' || !record.assigned.includes(user.key) || Date.parse(record.poll.from) > Date.now() || Date.parse(record.poll.to) <= Date.now()) fail(403, 'Forbidden');
                }
                return structuredClone(record.poll);
            }
            if (method === 'POST') {
                if (user.role !== 'student' || !record.assigned.includes(user.key) || Date.parse(record.poll.from) > Date.now() || Date.parse(record.poll.to) <= Date.now()) fail(403, 'Forbidden');
                fail(501, 'Submission DTO and anonymity contract need backend clarification');
            }
            if (!owns || user.role === 'student') fail(403, 'Forbidden');
            if (method === 'DELETE') { records.splice(records.indexOf(record), 1); return; }
            if (method === 'PATCH') {
                const value = await body(request);
                // The supplied PATCH allows content changes; business requirements prohibit them.
                if (value.name !== undefined || value.questions !== undefined) fail(400, 'Survey content is immutable; contract needs clarification');
                if (value.to === undefined) fail(400, 'Expected closing date');
                const to = text(value.to);
                if (!Number.isFinite(Date.parse(to)) || Date.parse(to) <= Date.parse(record.poll.from)) fail(400, 'Invalid date');
                record.poll.to = to;
                return;
            }
            fail(405, 'Method not allowed');
        }, request.method === 'POST' ? 201 : ['PATCH', 'DELETE'].includes(request.method) ? 204 : 200);
    };
}
