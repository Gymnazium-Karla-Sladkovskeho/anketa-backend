# Frontend

Web client for the school survey application. Teachers and administrators create surveys, teachers can read results, students fill them in. It is currently runs on mock data.

## Stack

| Area | Technology |
| --- | --- |
| Framework | SvelteKit 2, Svelte 5 (runes) |
| Language | TypeScript (strict) |
| Styling | Tailwind CSS v4 |
| Rendering | Client-side only (`ssr = false`) |
| UI language | Czech (default), English |

## Project structure

```text
src/
├── routes/                  # URLs only, thin files that connect a URL with a feature page
│   ├── (public)/            # login, change-password
│   ├── (student)/           # fill/, fill/[id]
│   ├── (teacher)/           # surveys/..., templates/
│   └── (admin)/admin/       # users, import, groups, surveys-overview
│
└── lib/
    ├── features/            # one folder per functional area, each with api/ components/ model/ index.ts
    │   ├── auth/  surveys/  survey-editor/  survey-filling/  templates/
    │   └── results/  users/  data-import/  student-groups/
    │
    └── core/              # code used by several features
        ├── api/  constants/  i18n/  styles/  types/  ui/  utils/
```

Folders in parentheses are route groups: they are not part of the URL and hold the role check for all pages inside.

Rules:

1. A feature imports only from `shared/`, never from another feature.
2. Code outside a feature imports from its `index.ts`, not from inner files.
3. Something used by one feature stays in it; it moves to `shared/` when a second feature needs it.

## Roles and routes

| Route | Page | Roles |
| --- | --- | --- |
| `/`, `/login`, `/change-password` | Landing, login, password change | everyone |
| `/fill`, `/fill/[id]` | Available surveys, filling a survey | student |
| `/surveys`, `/surveys/create`, `/surveys/[id]` | Own surveys, creating, detail | teacher, admin |
| `/surveys/[id]/results` | Results and PDF export | teacher, admin |
| `/templates` | Templates (admin also manages global ones) | teacher, admin |
| `/admin/users`, `/admin/import`, `/admin/groups` | Users, CSV import from Bakaláři, student groups | admin |
| `/admin/surveys-overview` | Titles, dates and questions of all surveys, no answers | admin |

The role is checked in the `+layout.ts` of each route group. The session is currently kept in memory, so a page reload logs the user out.

## State of features

| Feature | Status | Notes |
| --- | --- | --- |
| `auth` | In progress | Login, mock login, session, role guards done; password change is a placeholder |
| `shared/ui`, `shared/i18n` | Started | `Button`; login and landing texts |
| `surveys`, `survey-editor`, `survey-filling`, `templates` | Not started | Page placeholders exist |
| `results`, `users`, `data-import`, `student-groups` | Not started | |

Update this table when a feature changes status.

## Conventions

- **Data.** Requests live in `features/<name>/api/`. While the server is not ready, `USE_MOCKS` (`shared/constants`) returns mock data. Inputs are sent as strings; validation and answer anonymity are handled by the backend.
- **State.** Svelte 5 runes in `*.svelte.ts` files next to the feature. Global state only for the session and the language.
- **Styling.** Tailwind; global styles in `core/styles/app.css`. Visual design is not defined yet.
- **Texts.** All user-visible text goes through `t('key')`; add each key in both languages.

## Planned libraries

None are installed yet.

| Library | Purpose | License |
| --- | --- | --- |
| Chart.js | Charts of survey results | MIT |
| svelte-sonner | Toast notifications | MIT |
