import { api } from '$lib/core/api';
import type { Poll, PollPage, PollSummary } from '$lib/core/api/contracts';
import { USE_MOCKS } from '$lib/core/constants';
import type { FillingSurvey } from '../model/types';

export async function getAssignedSurveys(): Promise<PollSummary[]> {
    const polls: PollSummary[] = [];
    let page = 1;
    let totalPages = 1;
    do {
        const result = await api<PollPage>(`/poll/respondent?page=${page}&pageSize=25`);
        polls.push(...result.payload);
        totalPages = result.totalPages;
        page++;
    } while (page <= totalPages);
    return polls;
}

export async function getFillingSurvey(id: string): Promise<FillingSurvey> {
    if (!/^\d+$/.test(id) || !Number.isSafeInteger(Number(id)) || Number(id) < 1) throw new Error('Invalid poll ID');
    const poll = await api<Poll>(`/poll/${id}`);
    return {
        id: poll.id, name: poll.name, demo: USE_MOCKS,
        questions: (poll.questions ?? []).map((question, index) => ({
            ...question,
            // The screenshot demonstrates controls not specified by Swagger. Keep hints mock-only.
            ...(USE_MOCKS && poll.id === 1 && index === 2 ? { multiple: true } : {}),
            ...(USE_MOCKS && question.type === 'NUMERIC' ? { min: 0, max: 10 } : {})
        }))
    };
}
