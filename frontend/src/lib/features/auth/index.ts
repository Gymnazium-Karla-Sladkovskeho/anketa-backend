// Public API of the 'auth' feature. Other code imports only from here.
export { default as LoginPage } from './components/LoginPage.svelte';
export { session, logout } from './model/session.svelte';
export { homeFor } from './model/routes';
export { requireRole } from './guards';
export type { Role, User } from './model/types';

export { createAuthMock } from './api/auth.mock';
export { changePassword } from './api/auth.api';
