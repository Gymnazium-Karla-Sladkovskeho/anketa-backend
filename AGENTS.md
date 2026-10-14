# AGENTS.md

Instructions for AI coding agents working in this repository (OpenAI/Codex, Gemini, Claude, Copilot, Cursor and others).
This file is plain Markdown and needs no tool-specific features. If you cannot run commands or edit files in your environment, say so and give the user the exact commands or diff instead of pretending you did.

---

## 1. Project

**Anketa GYKAS** (working name, final name TBD) is a small web application for a secondary school (Gymnázium Karla Sladkovského). Students fill in feedback polls (ankety) about teachers, seminars, school activities and trips. Teachers and administrators create polls and view results.

- Client (objednatel): Gymnázium Karla Sladkovského.
- Contractor (zhotovitel): student team from FIT ČVUT (course BI-SP2).
- Size: **small**. Prefer simple, boring, maintainable solutions. Do not over-engineer (no microservices, no speculative abstractions, no features nobody asked for).
- The data are **personal data of students (partly minors)**. Privacy and anonymity are core requirements, not extras (see section 8).

> Project details are still being collected. Anything not documented in this file, in an ADR, or in the agreed requirements below is **undecided**. Treat it as an open question, not as permission to choose.

---

## 2. Working agreement (read this first)

1. **Do not guess. Ask.** If a request is ambiguous, conflicts with a documented decision, or touches something listed in section 3, stop and ask the user before implementing that part.
2. **Decisions live in writing.** Every non-trivial decision goes into an ADR (section 5). Every piece of work goes into the work log (section 4). The next agent or human must be able to continue without the chat history.
3. **Follow what is already agreed.** Accepted ADRs and agreed requirements beat your own preferences. If you think a decision is wrong, say so and propose a new ADR that supersedes it. Do not silently deviate.
4. **Keep changes small and reviewable.** One logical change per task. Do not mix refactoring, formatting and features.
5. **Be honest about verification.** Never claim that something builds, passes tests or works unless you actually ran it. Say what you ran and what you could not run.

### Start of every session (in this order)

1. Read this file.
2. List and read `documentation/dev/adr/` (all *Accepted* ADRs are binding).
3. Read the last ~5 entries of `documentation/dev/worklog.md` and all of `documentation/dev/open-questions.md`.
4. Read only the code and docs relevant to the task.

If any of these files or folders do not exist yet, create them using the templates in sections 4 and 5.

### Source of truth (highest priority first)

1. The user's current instruction in chat.
2. Accepted ADRs (`documentation/dev/adr/`).
3. Agreed requirements (section 6 of this file, derived from the meeting of 2026-06-30).
4. Other documents in `documentation/` (the technical-parameters questionnaire, `swagger.json`, `db.png`). These are **drafts** and may be outdated or inconsistent. See section 7.
5. Existing code.

If two sources of the same level conflict, or a draft conflicts with a higher level, **stop and ask**. Do not pick one silently.

---

## 3. Always ask the user before you

- Change, reinterpret or drop an agreed requirement (section 6) or an Accepted ADR.
- Change the **database schema**, the **API contract** (`swagger.json`) or the **module structure**.
- Touch **authentication, authorization, passwords, roles or anonymity** in any way.
- Add, remove or upgrade a **dependency**, framework, plugin or Gradle/JDK version.
- Choose a technology that is not chosen yet (web framework, database, frontend stack, PDF generation, etc.). Propose options in an ADR instead.
- Change CI/CD, container or deployment configuration.
- Delete, rename or move files and folders, or rewrite git history.
- Run `git commit`, `git push`, or open merge requests (only on explicit request).
- Handle anything that looks like real student, teacher or school data.
- Make a decision you would not be comfortable explaining in an ADR.

You may proceed **without asking** for: adding tests, fixing clear bugs inside one module, formatting and typo fixes, improving documentation, and refactoring inside a single module that does not change behaviour or public contracts. Record it in the work log anyway.

### How to ask

- Ask in the user's language (Czech by default).
- Group related questions. Make each one answerable quickly: give 2–3 options, mark your recommended option and say why in one sentence.
- Check the ADRs, requirements and open questions first. Never ask something that is already answered.
- Continue with independent work while waiting. Do not build on top of an unanswered question.
- **Non-interactive run (nobody to answer):** write the question to `open-questions.md`, choose the least irreversible option, label it clearly as an **assumption** in code comments, the work log and your final message.

---

## 4. Documentation duties

All development documentation lives under `documentation/dev/`. Public/user-facing documentation lives under `documentation/public/`.

| What | Where |
| :--- | :--- |
| Architecture decisions | `documentation/dev/adr/NNNN-short-title.md` |
| Work log / handoff notes | `documentation/dev/worklog.md` (append-only, newest entry on top) |
| Unresolved questions | `documentation/dev/open-questions.md` |
| API contract | `documentation/dev/swagger.json` |
| DB diagram | `documentation/dev/db.png` (plus an editable text source, see open questions) |
| Git workflow | `documentation/dev/GITLAB.md` |

**After every task**, before you finish:

1. Add an entry to `worklog.md`.
2. If you made or proposed a decision, write or update an ADR.
3. If you changed the API or the DB, update `swagger.json` / the DB diagram **in the same change** and say so in the log.
4. Add new unknowns to `open-questions.md`; remove or move answered ones into the relevant ADR.
5. If setup, build or run steps changed, update the relevant README.

### Work log entry template

```markdown
## YYYY-MM-DD – short title
- **Task:** what was requested
- **Done:** what changed (files / modules)
- **Decisions:** links to ADRs (or "none")
- **Assumptions:** anything you assumed because you could not ask
- **Verified:** commands you actually ran and their result (or "not run" + why)
- **Next:** what should happen next / what is blocked
```

### Open question format

```markdown
- [ ] **OQ-NNN** – question. *Context:* why it matters. *Options:* A / B. *Recommendation:* …  *Asked:* YYYY-MM-DD
```

---

## 5. Architecture Decision Records (ADR)

Use ADRs for any decision that is hard to reverse or that other people will have to live with: technology choices, data model, API shape, security and anonymity design, module boundaries, deployment.

**Rules**

- One file per decision: `documentation/dev/adr/NNNN-short-title.md` (`0001`, `0002`, …, never reuse numbers).
- You may create ADRs with status **Proposed**. Only the user can move an ADR to **Accepted**. Record the confirmation (date, "confirmed by user in chat").
- Never rewrite the substance of an Accepted ADR. To change a decision, write a new ADR with status *Accepted* after confirmation and mark the old one `Superseded by ADR-NNNN`.
- Keep ADRs short (about one page). Reference agreed requirements by their ID (e.g. `ANON-2`).
- Allowed statuses: `Proposed`, `Accepted`, `Rejected`, `Deprecated`, `Superseded by ADR-NNNN`.

**Template**

```markdown
# ADR-NNNN: Title

- **Status:** Proposed
- **Date:** YYYY-MM-DD
- **Related:** requirement IDs, issues (#123), other ADRs

## Context
What problem are we solving? What constraints apply (requirements, privacy, team size, deployment)?

## Options considered
1. Option A – pros / cons
2. Option B – pros / cons

## Decision
What we chose and why (one short paragraph).

## Consequences
What becomes easier, what becomes harder, what must be done next, what we still do not know.
```

Initial ADRs that are likely needed (propose them, do not decide them yourself): anonymity model and duplicate-submission prevention, authentication and password handling, web framework, database and migrations, PDF export approach, vulgarism filter approach, deployment and containers, project and module naming.

---

## 6. Agreed requirements

Source: meeting minutes of **2026-06-30** (client + team). Treat these as binding unless the user says otherwise. If you add a new requirement, write it here with the date and source.

### Roles (ROLE)
- **ROLE-1** There are exactly 3 roles: **student**, **teacher (učitel)**, **administrator**. (An earlier draft used "žák / učitel / vedení"; the meeting minutes supersede it.)
- **ROLE-2** *Student:* may only fill in polls available to them. Cannot create, delete or view other polls, and cannot see results.
- **ROLE-3** Student logs in with an **identification number** (identifikační číslo). The first password is **system-generated**; after the first login the student **must change it**.
- **ROLE-4** *Teacher:* creates polls, creates polls from templates, deletes own polls, adds and removes students in own polls, views results of own polls **after the poll has ended**, exports results to PDF, views results of own past polls. Cannot see or edit polls of other teachers or administrators.
- **ROLE-5** *Administrator:* has all teacher powers, plus: creates, edits and deletes **global templates**; adds and removes users; imports student data; can mark a teacher **inactive** (inactive teachers cannot create new polls).
- **ROLE-6** An administrator does **not** see the content or results of other people's polls, but does see their **names, creation dates and questions**.
- **ROLE-7** When a teacher is removed, **their polls are deleted**.
- **ROLE-8** Teacher data: first name and last name only.

### Polls (POLL)
- **POLL-1** There is **one poll type** in the implementation. Users distinguish kinds (teacher, seminar, school activity, trip, …) **by poll name only**.
- **POLL-2** A poll stores: author name, opening date, closing date, and the duration it stays open. A poll opens and closes on the given dates. There can be several polls per year.
- **POLL-3** The opening date cannot be moved after the poll has started. The closing date can be moved; a closed poll can be **reopened** by moving the closing date.
- **POLL-4** Questions are **open** (free text) or **closed**. Closed questions have an **even number of rating points** (no neutral middle).
- **POLL-5** Creating a poll: from scratch (no questions) or from a template.
- **POLL-6** From a **global template**, predefined questions **cannot be deleted**, but new questions can be added.
- **POLL-7** Users can create **local templates** from polls they created and edit them freely. Local templates appear in the "create from template" menu.
- **POLL-8** Deleting a poll is allowed only for its author and is **irreversible**.
- **POLL-9** Export: the author can export results to **PDF**. After exporting, offer to delete the poll from the system.
- **POLL-10** The author **cannot reply** to answers (they may discuss results with students offline, e.g. in class).
- **POLL-11** Results may be shown graphically (e.g. pie charts).

### Anonymity and content (ANON)
- **ANON-1** When answering, the student chooses: **fully anonymous** or **include their name**.
- **ANON-2** An anonymous student **must not be traceable in any way** (application, database, logs, exports). This is a hard design constraint.
- **ANON-3** Poll authors' results are private: other users cannot see them (see ROLE-6).
- **ANON-4** Open questions get a **simple filter for vulgarisms and inappropriate words**.

### Data and groups (DATA)
- **DATA-1** Initial data load via a **CSV export from the Bakaláři system**. Only administrators may do this.
- **DATA-2** Data changes over time: support **re-import** and **manual add/edit** of individuals.
- **DATA-3** Students are grouped by **class**. Teachers and administrators can also create **their own student groups**.

### Deployment and legal (OPS)
- **OPS-1** The application runs in **containers**.
- **OPS-2** Target: the client's Linux server, or alternatively a rented server/hosting.
- **OPS-3** A non-disclosure agreement (NDA) between contractor and client is planned because of sensitive student data. Licensing options for running outside the ČVUT environment are still to be investigated.

---

## 7. Known inconsistencies and open points (seed list)

Move these into `open-questions.md` and resolve them via the user and ADRs. Do not resolve them yourself.

- **Anonymity vs. one-answer-per-student.** ANON-2 forbids tracing anonymous answers, but the draft DB diagram links `Answers` directly to `Students`. How do we prevent duplicate submissions without storing who answered what? Needs an ADR before any answer-storage code is written.
- **Small-group de-anonymization.** Should results or comments be hidden below a minimum number of respondents?
- **Draft API/DB vs. requirements.** `swagger.json` and `db.png` predate the meeting. Known gaps: answers are not linked to question IDs; polls have no recipient (student group); template questions are missing in the DB; no security schemes; `close_question` and `numeric_question` have a wrong `type` enum; `/auth` requires `email` but sends `username`; login failure returns 404; the old "Poll_creators/Admins/Teachers" model does not match the 3 roles. Treat both files as drafts to be reconciled.
- **Identification number.** What exactly is the student's login ID (from Bakaláři)? What is the teacher/admin login?
- **Initial password delivery.** The draft API returns generated passwords in responses. Is that acceptable for the school's process?
- **Duration field.** POLL-2 lists both dates and a duration. Is the duration derived or stored?
- **Statistics.** The questionnaire proposed median, mean and most frequent grade, hiding statistics, reactions (thumbs up/down) and rating of whole classes/subjects. None of this was confirmed. Do not implement until agreed.
- **Data retention and GDPR.** Retention period, deletion policy (teacher removal deletes polls, see ROLE-7), consent for minors, who is the data controller.
- **Vulgarism filter.** Czech word list, behaviour on match (block, mask, flag), and how it interacts with anonymity.
- **Technology.** Web framework, database, frontend stack, PDF library, authentication mechanism, hosting. None are chosen yet.
- **Build state.** The backend snapshot appears to have no root build script and no Kotlin plugin version, and every module contains an IntelliJ sample `Main.kt` in package `org.example`. Verify that `./gradlew build` works before relying on it.

---

## 8. Privacy and security rules

- **Never** put real student, teacher or school data into the repository, test fixtures, logs, commit messages, issues or prompts to external services. Use clearly fake, generated data.
- **Never** commit secrets, passwords, tokens, or `.env` files with real values. Provide `.env.example` with placeholders.
- Do not log request bodies, answers, or anything that could link an anonymous answer to a person (IP address, user agent, timestamps precise enough to correlate, session IDs stored with answers). Ask before adding any logging or analytics.
- Store passwords only as strong salted hashes (use a vetted library; do not invent crypto). Enforce a password change on first login (ROLE-3).
- Enforce authorization **on the server** for every endpoint. Hiding a button in the UI is not access control.
- Validate and size-limit all input. Treat free-text answers as untrusted (escape on output, guard against injection and XSS).
- Prefer the privacy-preserving option whenever there is a choice, and write the trade-off into an ADR.

---

## 9. Repository and tech state

Monorepo (GitLab, FIT ČVUT):

```
backend/                 Kotlin/JVM backend (Gradle multi-module)
  gykas/                 Gradle root: settings.gradle.kts, gradlew, gradlew.bat
    domain/  application/  infrastructure/  web/  shared/
frontend/                empty so far (no stack chosen)
documentation/dev/       ADRs, work log, swagger.json, db.png, GITLAB.md
documentation/public/    user-facing docs
```

- Language: Kotlin, JVM toolchain **25**, Gradle wrapper **9.6.0**, Kotlin DSL build scripts, JUnit Platform with `kotlin("test")`.
- Intended layering (to be confirmed by ADR): `domain` has no outward dependencies; `application` depends on `domain`; `infrastructure` and `web` depend on `application`/`domain`; `shared` holds only truly shared primitives. Do not add dependencies between modules in a different direction without asking. Keep the module count justified for a small project.
- CI (`.gitlab-ci.yml`) is empty. Do not invent a pipeline without asking.

**Commands** (run from `backend/gykas`; on Windows use `gradlew.bat`). Confirm they work before documenting them as working:

```
./gradlew build
./gradlew test
```

---

## 10. Code conventions

- Code, identifiers, comments, commit messages and branch names are in **English**.
- ADRs, work log, open questions and user-facing text: **Czech** (user-facing UI text must be Czech). *(Default assumption; the user may change it.)*
- Kotlin: official code style (`kotlin.code.style=official`). Prefer immutability, small functions, explicit types at module boundaries, no wildcard imports.
- Keep business rules in `domain` / `application`, not in controllers or SQL.
- Write or update tests for behaviour you change, especially for **role permissions**, **poll date rules (POLL-3)** and **anonymity guarantees (ANON-2)**. These are the most important invariants.
- Public APIs follow `swagger.json`. Change the contract and the code together.
- Do not leave dead sample code, commented-out code, or unexplained TODOs. A TODO must reference an open question or issue.

---

## 11. Git and GitLab workflow

Full rules are in `documentation/dev/GITLAB.md`. Summary:

- **Issue first**: work starts from a GitLab issue. Small docs changes and meeting notes are exceptions.
- Branches: `feature/name-of-branch`, `fix/name-of-branch`.
- Commits: `type(scope): message`. Types: `feat`, `fix`, `refactor`, `chore`, `docs`, `ci`. Scopes: `api`, `ui`, `db`, `ci`, `docs`, `notes`. Reference the issue with `Refs: #123` in commits; `Closes #123` goes in the merge request description.
- Merge requests need at least 1 approval and a reference to an issue.
- Do not commit or push unless the user asks. Suggest a commit message instead.

---

## 12. Definition of done

A task is done only when all of these are true (or you have said explicitly which are not):

- [ ] The change matches agreed requirements and Accepted ADRs.
- [ ] Questions that needed the user were asked, not guessed.
- [ ] Build and tests were run, and the result is stated honestly.
- [ ] Tests cover the new or changed behaviour.
- [ ] `swagger.json` / DB diagram / READMEs are updated if affected.
- [ ] ADR written or updated for any real decision.
- [ ] `worklog.md` and `open-questions.md` are updated.
- [ ] No real personal data, secrets or sensitive logging were introduced.

---

## 13. Project information still to be added

*(User fills this in later. Agents: do not invent content here.)*

- Final project name:
- Team members and responsibilities:
- Deadlines and milestones:
- Hosting decision:
- Contact person at the school:
- Additional requirements:
