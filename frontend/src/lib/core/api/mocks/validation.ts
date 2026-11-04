import type { Question, TemplateInput } from '../contracts';
import { fail, object, text } from './helpers';

export function questions(value: unknown): Question[] {
    if (!Array.isArray(value)) fail(400, 'Expected questions array');
    return value.map(item => {
        const q = object(item);
        const name = text(q.name);
        if (q.type === 'OPEN' || q.type === 'NUMERIC') return { type: q.type, name };
        if (q.type === 'CLOSE' && Array.isArray(q.choices) && q.choices.length > 0) {
            return { type: 'CLOSE', name, choices: q.choices.map(choice => ({ name: text(object(choice).name) })) };
        }
        return fail(400, 'Invalid question');
    });
}

export function templateInput(input: Record<string, unknown>): TemplateInput {
    return { name: text(input.name), questions: questions(input.questions) };
}
