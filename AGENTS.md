# AGENTS.md

Guidance for AI coding agents in this repository (Claude, Codex, Gemini, Copilot, Cursor, …).
Reply to the user in their language (Czech by default); write code, identifiers, commits and docs in English.

## Project

**Anketa GYKAS** — a feedback-poll web app for Gymnázium Karla Sladkovského, built by a FIT ČVUT
student team (course BI-SP2). Students fill in polls; teachers and administrators create polls and
read results. The project is small: choose the simple, boring solution.

The data are personal data of students, many of them minors. **Anonymity is a hard constraint**: an
anonymous answer stays untraceable in code, database, logs and exports.

## Where things are

| Path | Contents | Read it |
| :--- | :--- | :--- |
| `backend/README.md` | Ktor 3 + Exposed + PostgreSQL; feature-slice layout, run/build commands | before any backend change |
| `frontend/README.md` | SvelteKit 2 + Svelte 5 + Tailwind 4; feature layout, conventions | before any frontend change |
| `documentation/dev/adr/` | Architecture decisions; **Accepted** ones are binding | before a structural, security or data-model change |
| `documentation/dev/GITLAB.md` | Issue-first GitLab flow, branch names, commit format | before creating a branch or proposing a commit |
| `documentation/dev/swagger.json`, `db.png`, `backend/scheme.sql` | Early **drafts**; they disagree with each other and with the code | as discussion input, never as the contract |

## Approval gate

Actions that are irreversible or affect teammates need the user's explicit approval **in the current
conversation** — an earlier approval covers only what it named:

- `git commit` and `git push` — always, each time; `merge`, `rebase`, `reset`, branch deletion, opening or merging MRs
- deleting or moving files; dropping or overwriting data (`backend/scheme.sql` starts with `DROP TABLE`)
- changing the DB schema, the HTTP API, authentication, roles, passwords or anonymity handling
- adding or upgrading a dependency, Gradle/JDK/Kotlin/Node version, CI or deployment config
- choosing between options that no ADR or user instruction settles
- anything that looks like real student, teacher or school data

Free to do: read anything, run builds and tests, edit code and docs within the task, add tests.
Finish with a proposed commit message instead of committing.

When asking, offer 2–3 options and mark your recommendation with a one-line reason. With nobody to
answer (non-interactive run), take the most reversible option and list it as an **assumption** in
your final message.

## Rules

- **Precedence:** the user's current instruction › Accepted ADRs › existing code › drafts. On a
  conflict between them, stop and ask.
- **Decisions in writing:** a choice that is hard to reverse or that others must live with gets a
  short ADR with status `Proposed` (format in `documentation/dev/adr/README.md`). Only the team
  accepts it.
- **Small changes:** one concern per change; refactoring, formatting and features travel separately.
- **Server-side authorization** on every non-public endpoint; the UI hiding a button is cosmetic.
- **Privacy:** log metadata only — request bodies, answers, passwords, tokens and anything linking an
  answer to a person stay out of logs. Test data is visibly fake.
- **Secrets** live in the root `.env`; code reads them from config.
- **Tests** accompany changed behaviour, above all role permissions, poll date rules
  and anonymity.
- **UI text** is Czech and goes through the i18n `t('key')` helper.

## Definition of done

- [ ] Verified: `./gradlew build` in `backend/` and/or `npm run check` in `frontend/` — report the
      commands you ran and their result, or say plainly what was not run and why.
- [ ] Accepted ADRs respected; new decisions recorded as `Proposed` ADRs.
- [ ] The relevant README updated if setup, structure or commands changed.
- [ ] Open questions surfaced to the user, not silently resolved.
- [ ] Commit message proposed per `GITLAB.md`.
