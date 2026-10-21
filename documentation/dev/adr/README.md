# Architecture Decision Records

One file per decision: `NNNN-short-title.md`, numbered upward, numbers never reused. About one page.

- **Statuses:** `Proposed`, `Accepted`, `Rejected`, `Superseded by ADR-NNNN`.
- Anyone (agents included) may write a `Proposed` ADR. Only the team accepts one; record who and when.
- An Accepted ADR keeps its substance. To change course, write a new ADR and mark the old one
  `Superseded by ADR-NNNN`.

## Template

```markdown
# ADR-NNNN: Title

- **Status:** Proposed
- **Date:** YYYY-MM-DD
- **Related:** requirement IDs, issues, ADRs

## Context
The problem and the constraints.

## Decision
What we do, in a few sentences.

## Consequences
What follows — easier, harder, still unknown.
```
