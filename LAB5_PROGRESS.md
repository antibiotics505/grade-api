# Lab 5 personal practice — 2026-10-20

This is a personal, AI-assisted implementation in antibiotics505's fork. It is not evidence of teammate participation, live API connectivity, or independent student mastery.

## Implemented

- Task 0: personal fork and local working copy.
- Task 1: `example.GetToken` accepts a username argument and saves a returned token to ignored `token.txt`, without printing it. **Not executed:** the owner chose offline-only validation. The GUI reports whether a token is configured without displaying its value.
- Task 3a: fetch each member's grades, include only the requested course, return the arithmetic mean; return zero when there are no matching records.
- Task 3b: authenticated GET `/team`, parse the team name and member array, surface API/JSON/I/O failures.
- Task 4: wire `GetTopGradeUseCase` through `Config` and the real My Team Swing button, including result/error dialogs.
- Java 11 compatibility: remove the unconditional newer-JDK-only test JVM flag from the starter POM.

## Verified locally

- `mvn test`: 9 tests, 0 failures, 0 errors. The two upstream tests are unchanged.
- Synthetic data covers course filtering, missing-course members, empty teams, no matching course, successful team JSON, API errors, malformed JSON, I/O failure, and the outgoing request method/path/token header.
- `validation/GuiLabCheck.java` triggers the real Swing buttons with a synthetic database. Actual dialogs: `Average Grade: 83.0` and `Top Grade: 85.0` for fictitious scores 81 and 85.
- All HTTP calls in tests are mocked. **No external test accounts, practice teams, real tokens, or grade records were created.**

## Commands / workflow

1. Open the project in IntelliJ as Maven; select JDK 11 or later and sync `pom.xml`.
2. Run `mvn test` for offline validation.
3. See `validation/README.md` for the graphical button check.
4. For a future, separately authorized live lab session, run `example.GetToken` with a chosen practice username; keep `token.txt` local, set the `token` environment variable in the `app.gui.Application` run configuration, and start that class.
5. Within the practice app, form/join the agreed practice team, log only intended practice data, and check Grade / Average / Top. This does not change Quercus team membership.

## Team work still needs actual collaboration

- Agree which repository the real team will share; invite actual teammates and exchange/review branches there. No collaborator permissions were changed here.
- The in-app team formation and teammate grade lookup were not performed during offline-only validation.
- Project API Exploration is separate: agree on a domain and user stories, identify at least a couple of relevant candidate APIs, try them or read enough documentation, and update the real team's README. No project domain or API has been selected on behalf of the team.
- Task 5 is optional bonus and was not required for this implementation.

Sources: https://q.utoronto.ca/courses/444184/pages/lab-5-instructions and upstream README at https://github.com/CSC207-2026F-UofT/grade-api .
