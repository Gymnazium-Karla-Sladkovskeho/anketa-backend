/** Mocks are enabled by default. Set VITE_USE_MOCKS=false for the Kotlin backend. */
export const USE_MOCKS = import.meta.env.VITE_USE_MOCKS !== 'false';
export const API_URL: string = import.meta.env.VITE_API_URL ?? '/api';
