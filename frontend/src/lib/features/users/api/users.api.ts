import { api } from '$lib/core/api';
import { query } from '$lib/core/api/query';
import type { GeneratedCredentials, Student, StudentInput, Teacher, TeacherInput } from '$lib/core/api/contracts';

export const usersApi = {
    students: (classes: string[]) => api<Student[]>(`/student${query({ classes })}`),
    student: (id: number) => api<Student>(`/student/${id}`),
    createStudent: (value: StudentInput) => api<GeneratedCredentials>('/student', { method: 'POST', body: JSON.stringify(value) }),
    updateStudent: (id: number, value: StudentInput) => api(`/student/${id}`, { method: 'PUT', body: JSON.stringify(value) }),
    teachers: (page = 1, pageSize = 25) => api<Teacher[]>(`/teacher${query({ page, pageSize })}`),
    teacher: (id: number) => api<Teacher>(`/teacher/${id}`),
    createTeacher: (value: TeacherInput) => api<GeneratedCredentials>('/teacher', { method: 'POST', body: JSON.stringify(value) }),
    updateTeacher: (id: number, value: TeacherInput & { active: boolean }) => api(`/teacher/${id}`, { method: 'PUT', body: JSON.stringify(value) }),
    remove: (type: 'student' | 'teacher', id: number) => api(`/${type}/${id}`, { method: 'DELETE' }),
    resetPassword: (type: 'student' | 'teacher', id: number) => api<GeneratedCredentials>(`/auth/password/${id}${query({ type })}`, { method: 'POST' })
};
