import type { Role } from './types';

export const homeFor = (role: Role) => (role === 'student' ? '/fill' : '/surveys');
