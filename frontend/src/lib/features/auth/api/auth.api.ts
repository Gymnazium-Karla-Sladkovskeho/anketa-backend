import { api } from '$lib/shared/api';
import { USE_MOCKS } from '$lib/shared/constants';
import { sleep } from '$lib/shared/utils';
import type { Credentials, User } from '../model/types';

export async function login(c: Credentials): Promise<User> {
	if (USE_MOCKS) {
		await sleep(1200);
		if (c.password === 'wrong') throw new Error('invalid credentials');
		const role = c.login === 'admin' ? 'admin' : c.login === 'teacher' ? 'teacher' : 'student';
		return { id: '1', name: c.login, role, mustChangePassword: false };
	}
	return api<User>('/auth/login', { method: 'POST', body: JSON.stringify(c) });
}
