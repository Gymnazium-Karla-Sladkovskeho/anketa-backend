/** OpenAPI form queries with explode=false for array parameters. */
export function query(values: Record<string, string | number | boolean | readonly (string | number)[] | undefined>): string {
    const params = new URLSearchParams();
    for (const [key, value] of Object.entries(values)) {
        if (value !== undefined) params.set(key, Array.isArray(value) ? value.join(',') : String(value));
    }
    const result = params.toString();
    return result ? `?${result}` : '';
}
