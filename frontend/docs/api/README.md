# API contracts and mocks

`swagger.json` is an unchanged snapshot supplied on 2026-10-27. It is a draft, not a complete backend contract. No requests to a real backend were made while implementing these mocks.

## Running

Mocks are enabled by default. Copy `.env.example` to `.env.local` if you need local overrides:

```dotenv
VITE_USE_MOCKS=true
VITE_API_URL=/api
```

For the Kotlin service, set `VITE_USE_MOCKS=false` and the agreed API base URL, then restart Vite. For a different origin, the backend must allow the frontend origin and Authorization header via CORS. There is no fallback to mocks when a real request fails.

| Username | Password | Role |
| --- | --- | --- |
| `student` | `demo` | Student, assigned polls 1, 2, 4 |
| `student2` | `demo` | Student, assigned poll 3 |
| `teacher` | `demo` | Author of polls 1, 2, 4 |
| `teacher2` | `demo` | Author of poll 3 |
| `admin` | `demo` | Administrator |

All records are synthetic. Arbitrary usernames/passwords no longer authenticate. There is no network request for mock credentials. Authentication, CRUD changes, passwords and records live in memory and reset on a full page reload. Dates of sample polls are relative to mock initialization: active, closed, future. IDs and account keys in the mock's internal participation fixtures are not added to response content.

## Structure

- `core/api/contracts.ts`: transport DTOs shared where necessary, separate from UI models.
- `core/api/client.ts`: JSON and Blob requests, Bearer header, typed HTTP errors, empty 201/204 handling. Tokens stay in memory.
- `core/api/mocks/`: Request/Response dispatcher, validation helpers, synthetic account context. Does not import features or monkey-patch global fetch.
- `features/*/api/*.mock.ts`: domain handlers; features do not import each other's handlers.
- `features/*/api/*.api.ts`: typed API functions used with either transport.
- `routes/mock-api.ts`: composition and deployment-prefix normalization; lazy initialization in root layout.
- `scripts/mock-api.test.mjs`: integration checks using Vite's TypeScript/Svelte transform, without extra dependencies.

Unknown mock routes return 501 and never fall through to the network. HTTP validation errors use plain text because Swagger does not define an error DTO. Mutations return the documented status and body, including empty 201 responses. GET results are serialized copies; consumers cannot mutate internal fixtures. AbortSignal is supported at dispatch boundaries. The client doesn't invent latency.

## Coverage and provisional policy

Implemented: login/profile/password change/reset; personal/global template CRUD; student/teacher CRUD and teacher deactivation; student-list CRUD; own, global and assigned poll lists; poll details/create/delete/closing-date update; empty result list for a closed poll owned by the requester.

Provisional access policy comes from the project context, since Swagger lacks security declarations: users/password reset are admin-only; personal templates and polls belong to their author; teachers and admins can read global templates, only admins can mutate them; student lists belong to the creating teacher with admin access; student detail access to a poll requires assignment and an active date range; results are author-only after closure, including for admins. Deleting a teacher cascades their polls, personal templates and owned lists. Deleting students removes them from lists and assignment fixtures. Deactivation invalidates their mock tokens. This simulates access for frontend development and is not a production security implementation.

The poll assignment endpoint is missing. New polls therefore have no assigned students; only seed fixtures contain assignments. This does not silently assign a new poll to all students. Student list summaries do not reveal members of another teacher's list.

Student home loads assigned polls through GET /poll/respondent, including all pages. Active and closed sections use opening/closing dates; closed does not mean submitted. Future polls are hidden until opening. One active poll is available to student with four preview controls. The filling page loads GET /poll/{id}; response submission remains blocked and no answers are transmitted or stored. Multi-select on the third sample question and a 0–10 numeric scale are mock-only presentation hints in survey-filling/api/filling.api.ts, not additions to the Swagger DTO. Real numeric questions use an unconstrained number field until a range is agreed. Other business pages remain unfinished.

## Contract issues to resolve with the backend

| Area | Problem | Temporary handling |
| --- | --- | --- |
| POST `/auth` | Properties define `username`, but `required` lists `email` | Send `username` and `password` |
| GET `/auth/me` | No user ID or first-login flag; role enum absent, example is `žák` | UI user has only name/role. Accept `student`/`žák`, `teacher`/`učitel`, `admin`; reject unknown roles. Czech teacher/admin spelling needs confirmation |
| Security | Bearer appears in login response but security schemes and refresh/logout endpoints are absent | In-memory Bearer token, no persistence/refresh, logout clears local token/session |
| Required fields | Some schemas list nonexistent properties (student_list, id_student_list_br, id_poll_br_scroll); many fields lack required declarations | Follow actual properties/examples. DTOs are explicitly maintained, not advertised as generated. Mock validation requires usable names and template question arrays |
| Questions | CLOSE/NUMERIC schemas both say type OPEN, contradicting discriminator mapping | Follow mapping: OPEN, CLOSE, NUMERIC. No speculative rating limits or multiple-choice representation |
| Responses | `answers.items` combines type object with string/number oneOf and a missing required answer property | No submission/result DTO invented. POST `/poll/{id}` returns 501 for otherwise eligible students |
| Anonymity | No submission anonymity field or participation/completion protocol | No response storage, no privacy-critical implementation or anonymity guarantee |
| Survey lifecycle | PATCH allows name/questions, conflicting with immutable-content business rules; opening-date update missing | Mock permits closing-date change only; content changes return 400. No invented opening-date endpoint |
| Participants | No endpoint for assigning users/classes/lists to a poll | Static assignments in synthetic fixtures only |
| GET `/poll` groups | Meaning of groups identifiers unclear | Supplying groups returns 501 rather than silently ignoring it |
| GET `/poll/respondent` | No completion/answered flag; meaning of closed vs submitted not established | Return the specified page envelope without extra flags; UI must not infer submitted state from dates |
| PDF | MIME is defined, document layout/content unspecified | Blob client ready; authorized mock PDF export returns 501 rather than fake JSON/PDF |
| Admin authorship | Poll response requires a teacher, but admin identity has no teacher ID | Mock admin-created poll uses synthetic teacher ID 100, not a production assumption |
| Passwords | Complexity and initial-password policy not defined | Reject empty passwords only; no invented password policy |
| CSV imports | No import endpoints in this snapshot | No invented endpoint |

These adjustments are isolated in frontend DTOs/mocks. The source Swagger remains unchanged. Complete submission/PDF/privacy behavior only after agreeing on those contracts.

## Verification

```sh
npm run check
npm run test:mocks
npm run build
```

Tests cover login/profile, invalid credentials, password changes/reset, role and ownership restrictions, pagination, query validation, CRUD, empty responses, teacher deactivation/deletion, cascading cleanup, reset, unsupported routes and abort. They simulate behavior in memory; they do not certify real backend conformance or anonymity.
