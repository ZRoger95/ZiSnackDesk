# Git Commit Message Guideline

## Format
```
<type>(<scope>): <subject>

<body>

<footer>
```

## Types
| Type | Description |
|------|-------------|
| feat | New feature |
| fix | Bug fix |
| docs | Documentation only changes |
| style | Code style or formatting (no logic change) |
| refactor | Code change that neither fixes a bug nor adds a feature |
| test | Adding or updating tests |
| chore | Build, tooling, or dependency changes |

## Scopes
Use a short scope to indicate the area affected. Common scopes for this repository:

- controller
- service
- mapper
- model
- config
- db
- redis
- docker
- ci
- docs
- build
- deps
- test

Omit the scope for repository-wide or cross-cutting changes.

## Subject (short summary)
- Use the imperative mood (e.g., "add", "fix", "update").
- Start with a lowercase verb.
- No trailing period.
- Limit to 50 characters or fewer.

## Body (optional)
- Explain the motivation for the change and summarize the implementation.
- Wrap lines at 72 characters.
- Use bullet points for multi-line lists or notable details.

## Footer (optional)
Use the footer for metadata such as breaking changes or references:

```
BREAKING CHANGE: describe the breaking change and migration steps
Refs: #123
```

## Examples
```
feat(service): add login endpoint with JWT support

- add JwtFilter for token verification
- update SysUser service and DTOs
```

```
fix(mapper): correct user query logic to handle null values

use Optional to avoid NPE
```

## Notes
- Keep commit messages clear and focused: one reason per commit when possible.
- For large features, prefer multiple small, reviewable commits.
