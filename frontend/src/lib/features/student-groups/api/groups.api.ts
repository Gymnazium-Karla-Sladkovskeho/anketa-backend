import { api } from '$lib/core/api';
import type { StudentList, StudentListInput, StudentListSummary } from '$lib/core/api/contracts';

export const groupsApi = {
    list: () => api<StudentListSummary[]>('/student-list'),
    get: (id: number) => api<StudentList>(`/student-list/${id}`),
    create: (value: StudentListInput) => api('/student-list', { method: 'POST', body: JSON.stringify(value) }),
    update: (id: number, value: StudentListInput) => api(`/student-list/${id}`, { method: 'PUT', body: JSON.stringify(value) }),
    remove: (id: number) => api(`/student-list/${id}`, { method: 'DELETE' })
};
