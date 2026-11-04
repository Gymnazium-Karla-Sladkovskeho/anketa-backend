import { USE_MOCKS } from '$lib/core/constants';

// Pure SPA: the backend is a separate Kotlin service.
export const ssr = false;
let mocksReady: Promise<void> | undefined;

export async function load() {
    if (USE_MOCKS) {
        mocksReady ??= import('./mock-api').then(({ initializeMocks }) => initializeMocks());
        await mocksReady;
    }
}
