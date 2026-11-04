import { clearAccessToken } from '$lib/core/api';
import type { User } from './types';

/** Session and access token are held in memory and lost on reload. */
export const session = $state<{ user: User | null }>({ user: null });

export function logout() {
    clearAccessToken();
    session.user = null;
}
