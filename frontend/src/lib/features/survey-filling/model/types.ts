import type { Question } from '$lib/core/api/contracts';

/** Presentation hints only; these are not additions to the backend response DTO. */
export type FillingQuestion = Question & { multiple?: boolean; min?: number; max?: number };
export type FillingSurvey = { id: number; name: string; questions: FillingQuestion[]; demo: boolean };
