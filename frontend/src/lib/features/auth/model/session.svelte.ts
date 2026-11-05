import type { User } from './types';

/** In-memory session. Lost on reload until the backend provides a session cookie / token. */
export const session = $state<{ user: User | null }>({ user: null });
