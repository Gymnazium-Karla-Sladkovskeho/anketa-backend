# SP2 / Anketa GYKAS — Frontend

School survey application for Gymnázium Karla Sladkovského. The frontend is developed separately from the Kotlin backend and currently uses an in-memory mock API based on the supplied Swagger document.

## Run locally

```sh
npm ci
npm run dev
```

Open the local URL printed by Vite. Mocks are enabled by default; no backend is required.

| Username | Password | Role |
| --- | --- | --- |
| `student`, `student2` | `demo` | Student |
| `teacher`, `teacher2` | `demo` | Teacher |
| `admin` | `demo` | Administrator |

Other credentials are rejected. The session, Bearer token and mock data are kept in memory. Refreshing the page resets the data and requires signing in again. Session restoration is not implemented for either transport.

## Current functionality

| Area | Implemented | Remaining |
| --- | --- | --- |
| Authentication | Login through `POST /auth` and `GET /auth/me`, logout, role guards | Session restoration and password-change screen |
| Student home | Assigned surveys loaded from the API; active and closed sections | Submission/completion tracking |
| Survey form | Responsive preview with text, single-choice, demo multiple-choice and rating controls | Sending and storing responses |
| Teacher home | Own surveys grouped into inactive, active and closed sections | Creation, survey details and results screens |
| Templates, users, student groups | Typed API functions and mock CRUD handlers | Functional management screens |
| Results and PDF | Mock returns an empty result list for an authorized author of a closed survey; Blob transport is available | Results UI, aggregation and PDF export |
| CSV import | Route placeholder | API contract and implementation |

`student` has one active demonstration survey with four questions. Its multiple-choice control and 0–10 rating scale are presentation settings for the mock, not finalized backend fields. Form submission displays a notice and does not send or save answers.

Inactive teacher surveys have an opening date in the future. They are scheduled surveys, not editable drafts. Closed surveys are determined by their closing date; this does not indicate whether a student submitted a response.

## Interface

- Czech by default, with an English language switch. Survey content stays in the language supplied by the API.
- Student and teacher screens use the system theme initially; the theme button manually switches between light and dark. The selection survives navigation within the role layout and resets on reload.
- Login uses the supplied SVG wordmark and bell; the bell is also the browser favicon. The formula background is shown on wide screens only.
- Student sections stack and can collapse on mobile. Teacher sections stack, with the add-survey button above them on mobile and centered below them on desktop.
- Survey panels grow with their content. There is no internal list scrollbar or UI pagination; the page scrolls. The student list fetches all API pages. Teacher panels have equal heights within the desktop grid.

## Stack and structure

SvelteKit 2, Svelte 5 Runes, TypeScript in strict mode, Tailwind CSS v4 and Vite. Rendering is client-side only (`ssr = false`).

```text
src/
├── routes/
│   ├── (public)/             # Login and password-change placeholder
│   ├── (student)/            # /fill and /fill/[id]
│   ├── (teacher)/            # /surveys, /surveys/..., /templates
│   ├── (admin)/              # /admin/...
│   └── mock-api.ts           # Composition of feature mock handlers
└── lib/
    ├── features/             # auth, surveys, survey-editor, survey-filling,
    │                         # templates, results, users, data-import, student-groups
    ├── core/                 # api, constants, i18n, styles, ui, utils
    └── assets/               # SVG branding, favicon and login background

docs/api/                     # Swagger snapshot and contract notes
scripts/mock-api.test.mjs     # Mock API integration checks
```

Dependency direction: `routes → features → core`. Routes compose features and handle routing, loading and role layouts. Feature consumers import through the feature's `index.ts`; feature implementation details remain private. Reusable UI belongs in `core/ui`, including `Button`, `Brand` and `DashboardShell`. Feature folders contain `api/`, `components/` and `model/` only as needed.

User-facing interface text uses `t('key')` from `core/i18n`, with entries in both Czech and English. Styles use Tailwind and component CSS; SCSS is not used.

## Roles and routes

Route groups do not appear in URLs. Their `+layout.ts` files check the user's role.

| Routes | Roles | Screen state |
| --- | --- | --- |
| `/`, `/login` | Everyone | Landing and login |
| `/change-password` | Everyone | Placeholder |
| `/fill`, `/fill/[id]` | Student | Survey list and form preview |
| `/surveys` | Teacher, admin | Own survey list |
| `/surveys/create`, `/surveys/[id]`, `/surveys/[id]/results` | Teacher, admin | Placeholders |
| `/templates` | Teacher, admin | Placeholder |
| `/admin/users`, `/admin/import`, `/admin/groups`, `/admin/surveys-overview` | Admin | Placeholders |

Frontend role guards control navigation. The backend must independently enforce authorization. The admin overview is intended to expose survey metadata, not other authors' results.

## API and mocks

Copy `.env.example` to `.env.local` to override the defaults:

```dotenv
VITE_USE_MOCKS=true
VITE_API_URL=/api
```

To use the Kotlin service, set `VITE_USE_MOCKS=false`, configure the agreed API base URL and restart Vite. Real requests do not fall back to mocks. Backend integration has not yet been verified.

Both transports use the same client and feature API functions. Mock handlers use synthetic data, check roles and ownership, and support CRUD and pagination where documented. Unknown mock endpoints return `501` instead of reaching the network.


## Checks and build

```sh
npm run check       # Svelte and TypeScript diagnostics
npm run test:mocks  # Mock API integration checks
npm run build      # Production build
npm run preview    # Preview the production build
```

Mock tests cover authentication, role and ownership restrictions, CRUD, pagination, survey date boundaries, cascading cleanup, token expiry and unsupported operations. They do not validate a running Kotlin backend.

The project currently uses `adapter-auto`. Choose a deployment adapter and SPA routing configuration for the target hosting environment before deployment.
