/** In-memory only. Never persists authentication credentials to browser storage. */
let token: { value: string; expiresAt: number } | null = null;

export function setAccessToken(value: string, expiresIn: number) {
    token = { value, expiresAt: Date.now() + expiresIn * 1000 };
}

export function clearAccessToken() {
    token = null;
}

export function getAccessToken(): string | null {
    if (token && token.expiresAt <= Date.now()) token = null;
    return token?.value ?? null;
}
