import { API_URL } from '$lib/shared/constants';

export class ApiError extends Error {
	constructor(
		public status: number,
		message: string
	) {
		super(message);
	}
}

export async function api<T>(path: string, init: RequestInit = {}): Promise<T> {
	const res = await fetch(`${API_URL}${path}`, {
		credentials: 'include',
		headers: { 'Content-Type': 'application/json', ...init.headers },
		...init
	});
	if (!res.ok) throw new ApiError(res.status, res.statusText);
	return res.status === 204 ? (undefined as T) : ((await res.json()) as T);
}
