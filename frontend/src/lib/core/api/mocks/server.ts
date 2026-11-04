export type MockHandler = (request: Request) => Response | undefined | Promise<Response | undefined>;
let handlers: MockHandler[] = [];

export function configureMockHandlers(next: MockHandler[]) {
    handlers = next;
}

/** Same Request/Response boundary as fetch; unknown routes never reach the network. */
export async function mockFetch(request: Request): Promise<Response> {
    request.signal.throwIfAborted();
    for (const handler of handlers) {
        const response = await handler(request);
        request.signal.throwIfAborted();
        if (response) return response;
    }
    return new Response('Mock endpoint not implemented', { status: 501 });
}
