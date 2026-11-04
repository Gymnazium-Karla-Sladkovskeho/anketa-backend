import { ApiError } from '$lib/core/api';
import { getAssignedSurveys } from '$lib/features/survey-filling';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ parent }) => {
    await parent();
    try { return { polls: await getAssignedSurveys(), failed: false }; }
    catch (error) {
        if (error instanceof ApiError || error instanceof TypeError) return { polls: [], failed: true };
        throw error;
    }
};
