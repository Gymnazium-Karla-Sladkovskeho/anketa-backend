import { API_URL, USE_MOCKS } from '$lib/core/constants';
import { getAccessToken } from './token';

export class ApiError extends Error {
    constructor(public status: number, message: string) {
        super(message);
        this.name = 'ApiError';
    }
}

async function request(path: string, init: RequestInit): Promise<Response> {
    if (!path.startsWith('/') || path.startsWith('//')) throw new Error('Expected an API-relative path');
    const headers = new Headers(init.headers);
    if (typeof init.body === 'string' && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json');
    const token = getAccessToken();
    if (token && !headers.has('Authorization')) headers.set('Authorization', `Bearer ${token}`);
    const url = new URL(`${API_URL.replace(/\/$/, '')}${path}`, typeof location === 'undefined' ? 'http://localhost' : location.origin);
    const req = new Request(url, { credentials: 'include', ...init, headers });
    const response = USE_MOCKS
        ? await (await import('./mocks/server')).mockFetch(req)
        : await fetch(req);
    if (!response.ok) throw new ApiError(response.status, (await response.text()) || response.statusText || `HTTP ${response.status}`);
    return response;
}

export async function api<T = void>(path: string, init: RequestInit = {}): Promise<T> {
    const response = await request(path, init);
    // Swagger also documents empty 201 responses, not only empty 204 responses.
    const text = await response.text();
    return text ? JSON.parse(text) as T : undefined as T;
}

export async function apiBlob(path: string, init: RequestInit = {}): Promise<Blob> {
    return (await request(path, init)).blob();
}
