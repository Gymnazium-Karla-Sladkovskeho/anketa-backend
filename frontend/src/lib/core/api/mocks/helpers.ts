export class MockHttpError extends Error {
    constructor(public status: number, message: string) { super(message); }
}

export function fail(status: number, message: string): never {
    throw new MockHttpError(status, message);
}

export async function respond(action: () => unknown | Promise<unknown>, status = 200): Promise<Response> {
    try {
        const value = await action();
        return value === undefined ? new Response(null, { status }) : Response.json(value, { status });
    } catch (error) {
        if (error instanceof MockHttpError) return new Response(error.message, { status: error.status });
        throw error;
    }
}

export function object(value: unknown): Record<string, unknown> {
    if (!value || typeof value !== 'object' || Array.isArray(value)) fail(400, 'Expected object');
    return value as Record<string, unknown>;
}

export async function body(request: Request) {
    try { return object(await request.json()); }
    catch (error) {
        if (error instanceof MockHttpError) throw error;
        fail(400, 'Invalid JSON');
    }
}

export function text(value: unknown): string {
    if (typeof value !== 'string' || !value.trim()) fail(400, 'Expected non-empty string');
    return value;
}

export function id(value: unknown): number {
    const result = typeof value === 'string' ? Number(value) : value;
    if (typeof result !== 'number' || !Number.isSafeInteger(result) || result < 1) fail(400, 'Expected positive integer');
    return result;
}

export function pagination(url: URL) {
    return { page: id(url.searchParams.get('page') ?? 1), pageSize: id(url.searchParams.get('pageSize') ?? 25) };
}

export function slicePage<T>(items: T[], page: number, pageSize: number) {
    return structuredClone(items.slice((page - 1) * pageSize, page * pageSize));
}
