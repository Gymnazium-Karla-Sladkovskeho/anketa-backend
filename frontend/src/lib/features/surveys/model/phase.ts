import type { PollSummary } from '$lib/core/api/contracts';

/** Lifecycle derived from Swagger dates, not a persisted draft/publication status. */
export function pollPhase(poll: Pick<PollSummary, 'from' | 'to'>, now: number): 'inactive' | 'active' | 'closed' {
    if (Date.parse(poll.to) <= now) return 'closed';
    if (Date.parse(poll.from) > now) return 'inactive';
    return 'active';
}
