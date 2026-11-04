import { api, apiBlob } from '$lib/core/api';
import { query } from '$lib/core/api/query';
import type { Poll, PollInput, PollPage, PollSummary } from '$lib/core/api/contracts';

export const surveysApi = {
    own: () => api<PollSummary[]>('/poll'),
    assigned: (page = 1, pageSize = 25) => api<PollPage>(`/poll/respondent${query({ page, pageSize })}`),
    overview: (authors?: number[], page = 1, pageSize = 25) => api<PollPage>(`/poll/global${query({ id: authors, page, pageSize })}`),
    get: (id: number) => api<Poll>(`/poll/${id}`),
    create: (value: PollInput) => api('/poll', { method: 'POST', body: JSON.stringify(value) }),
    changeClosingDate: (id: number, to: string) => api(`/poll/${id}`, { method: 'PATCH', body: JSON.stringify({ to }) }),
    remove: (id: number) => api(`/poll/${id}`, { method: 'DELETE' }),
    // Mock returns 501 until the PDF specification is agreed; real transport supports the documented MIME type.
    pdf: (id: number) => apiBlob(`/poll/pdf/${id}`)
};
