/** DTOs for docs/api/swagger.json. Ambiguities are recorded in docs/api/README.md. */
export type LoginRequest = { username: string; password: string };
export type TokenResponse = { access_token: string; token_type: string; expires_in: number };
export type CurrentUserResponse = { name: string; lastname: string; role: string };
export type GeneratedCredentials = { username: string; password: string };
export type TeacherInput = { name: string; lastname: string };
export type Teacher = TeacherInput & { id: number; active: boolean };
export type StudentInput = TeacherInput & { class?: string };
export type Student = StudentInput & { id: number };
export type StudentListInput = { name?: string; students: { id: number }[] };
export type StudentList = { id: number; name?: string; students: Student[] };
export type StudentListSummary = { id: number; name?: string };
// Follow the discriminator mapping; the CLOSE/NUMERIC enums are typos in the supplied spec.
export type Question =
    | { type: 'OPEN'; name: string }
    | { type: 'CLOSE'; name: string; choices: { name: string }[] }
    | { type: 'NUMERIC'; name: string };
export type TemplateInput = { name: string; questions: Question[] };
export type Template = TemplateInput & { id: number };
export type TemplateSummary = Pick<Template, 'id' | 'name'>;
export type PollInput = { name: string; from: string; to: string; questions?: Question[] };
export type PollSummary = Omit<PollInput, 'questions'> & { id: number; teacher: Teacher };
export type Poll = PollSummary & { questions?: Question[] };
export type PollPage = { page: number; totalPages: number; pageSize: number; payload: PollSummary[] };
// Response submission DTO intentionally omitted: poll_resp_cr is contradictory and lacks anonymity.
