import { requireRole } from '$lib/features/auth';

export const load = () => requireRole('teacher', 'admin');
