/** Shared mock infrastructure, not production authentication or an anonymity design. */
type Role = 'student' | 'teacher' | 'admin';
import { fail } from './helpers';

export type MockIdentity = { key: string; name: string; lastname: string; role: Role; entityId?: number };
export type MockAccount = MockIdentity & { username: string; password: string; active: boolean };

export function createMockContext() {
    const accounts: MockAccount[] = [
        { key: 'student-1', username: 'student', password: 'demo', name: 'Demo', lastname: 'Student', role: 'student', entityId: 1, active: true },
        { key: 'student-2', username: 'student2', password: 'demo', name: 'Test', lastname: 'Student', role: 'student', entityId: 2, active: true },
        { key: 'teacher-1', username: 'teacher', password: 'demo', name: 'Demo', lastname: 'Teacher', role: 'teacher', entityId: 1, active: true },
        { key: 'teacher-2', username: 'teacher2', password: 'demo', name: 'Test', lastname: 'Teacher', role: 'teacher', entityId: 2, active: true },
        { key: 'admin-1', username: 'admin', password: 'demo', name: 'Demo', lastname: 'Admin', role: 'admin', active: true }
    ];
    const tokens = new Map<string, { key: string; expiresAt: number }>();
    const removalListeners: ((account: MockAccount) => void)[] = [];
    const updateListeners: ((account: MockAccount) => void)[] = [];
    return {
        accounts,
        issue(account: MockAccount) {
            const token = `mock-${crypto.randomUUID()}`;
            tokens.set(token, { key: account.key, expiresAt: Date.now() + 86400_000 });
            return { access_token: token, token_type: 'Bearer', expires_in: 86400 };
        },
        requireUser(request: Request, roles?: Role[]) {
            const authorization = request.headers.get('Authorization');
            const token = authorization?.startsWith('Bearer ') ? tokens.get(authorization.slice(7)) : undefined;
            const user = token && token.expiresAt > Date.now() ? accounts.find(a => a.key === token.key && a.active) : undefined;
            if (!user) fail(401, 'Unauthorized');
            if (roles && !roles.includes(user.role)) fail(403, 'Forbidden');
            return user;
        },
        revoke(key: string) {
            for (const [token, record] of tokens) if (record.key === key) tokens.delete(token);
        },
        onAccountRemoved(listener: (account: MockAccount) => void) { removalListeners.push(listener); },
        onAccountUpdated(listener: (account: MockAccount) => void) { updateListeners.push(listener); },
        accountUpdated(account: MockAccount) { updateListeners.forEach(listener => listener(account)); },
        removeAccount(account: MockAccount) {
            const index = accounts.indexOf(account);
            if (index >= 0) accounts.splice(index, 1);
            for (const [token, record] of tokens) if (record.key === account.key) tokens.delete(token);
            removalListeners.forEach(listener => listener(account));
        }
    };
}
export type MockContext = ReturnType<typeof createMockContext>;
