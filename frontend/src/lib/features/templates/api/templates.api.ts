import { api } from '$lib/core/api';
import { query } from '$lib/core/api/query';
import type { Template, TemplateInput, TemplateSummary } from '$lib/core/api/contracts';

const path = (global: boolean, id?: number) => `/template${global ? '/global' : ''}${id === undefined ? '' : `/${id}`}`;
export const templatesApi = {
    list: (includeGlobal = true) => api<TemplateSummary[]>(`/template${query({ getAll: includeGlobal })}`),
    listGlobal: () => api<TemplateSummary[]>('/template/global'),
    get: (id: number, global = false) => api<Template>(path(global, id)),
    create: (value: TemplateInput, global = false) => api(path(global), { method: 'POST', body: JSON.stringify(value) }),
    update: (id: number, value: TemplateInput, global = false) => api(path(global, id), { method: 'PUT', body: JSON.stringify(value) }),
    remove: (id: number, global = false) => api(path(global, id), { method: 'DELETE' })
};
