import { api, clearAccessToken, setAccessToken } from '$lib/core/api';
import type { CurrentUserResponse, TokenResponse } from '$lib/core/api/contracts';
import type { Credentials, Role, User } from '../model/types';

function normalizeRole(role: string): Role {
    switch (role) {
        case 'student': case 'žák': return 'student';
        case 'teacher': case 'učitel': return 'teacher';
        case 'admin': return 'admin';
        default: throw new Error(`Unsupported user role: ${role}`);
    }
}

export async function login(credentials: Credentials): Promise<User> {
    clearAccessToken();
    try {
        const token = await api<TokenResponse>('/auth', {
            method: 'POST', body: JSON.stringify({ username: credentials.login, password: credentials.password })
        });
        if (!token.access_token || token.token_type?.toLowerCase() !== 'bearer' || !(token.expires_in > 0)) {
            throw new Error('Invalid authentication response');
        }
        setAccessToken(token.access_token, token.expires_in);
        const profile = await api<CurrentUserResponse>('/auth/me');
        return { name: `${profile.name} ${profile.lastname}`.trim(), role: normalizeRole(profile.role) };
    } catch (error) {
        clearAccessToken();
        throw error;
    }
}

export const changePassword = (password: string) => api('/auth/password', { method: 'PATCH', body: JSON.stringify({ password }) });
