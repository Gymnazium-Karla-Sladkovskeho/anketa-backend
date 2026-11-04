import { ApiError } from '$lib/core/api';
import { getFillingSurvey } from '$lib/features/survey-filling';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ params, parent }) => {
    await parent();
    if (!/^\d+$/.test(params.id) || !Number.isSafeInteger(Number(params.id)) || Number(params.id) < 1) return { survey: null, failed: true };
    try { return { survey: await getFillingSurvey(params.id), failed: false }; }
    catch (error) {
        if (error instanceof ApiError || error instanceof TypeError) return { survey: null, failed: true };
        throw error;
    }
};
