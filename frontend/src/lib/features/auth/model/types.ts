export type Role = 'student' | 'teacher' | 'admin';
// /auth/me does not currently provide an ID or a first-login password-change flag.
export type User = { name: string; role: Role };
export type Credentials = { login: string; password: string };
