/** Route-level composition keeps core independent of features and features independent of each other. */
import { API_URL } from '$lib/core/constants';
import { configureMockHandlers } from '$lib/core/api/mocks/server';
import { createMockContext } from '$lib/core/api/mocks/context';
import { createAuthMock } from '$lib/features/auth';
import { createUsersMock } from '$lib/features/users';
import { createGroupsMock } from '$lib/features/student-groups';
import { createTemplatesMock } from '$lib/features/templates';
import { createSurveysMock } from '$lib/features/surveys';

export function initializeMocks() {
    const context = createMockContext();
    const handlers = [createAuthMock(context), createUsersMock(context), createGroupsMock(context), createTemplatesMock(context), createSurveysMock(context)];
    const basePath = new URL(API_URL, 'http://localhost').pathname.replace(/\/$/, '');
    configureMockHandlers(handlers.map(handler => request => {
        const url = new URL(request.url);
        // Feature handlers use the paths exactly as written in Swagger, without the deployment prefix.
        if (basePath && !url.pathname.startsWith(`${basePath}/`)) return;
        url.pathname = url.pathname.slice(basePath.length) || '/';
        return handler(new Request(url, request));
    }));
}
