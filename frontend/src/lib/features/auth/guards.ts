import { redirect } from '@sveltejs/kit';
import { session } from './model/session.svelte';
import { homeFor } from './model/routes';
import type { Role } from './model/types';

/** Call from the +layout.ts load function of a route group. */
export function requireRole(...roles: Role[]) {
	const user = session.user;
	if (!user) redirect(307, '/login');
	if (!roles.includes(user.role)) redirect(307, homeFor(user.role));
}
