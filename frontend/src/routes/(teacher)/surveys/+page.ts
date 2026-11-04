import { ApiError } from '$lib/core/api';
import { surveysApi } from '$lib/features/surveys';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ parent }) => {
    await parent();
    try { return { polls: await surveysApi.own(), failed: false }; }
    catch (error) {
        if (error instanceof ApiError || error instanceof TypeError) return { polls: [], failed: true };
        throw error;
    }
};
