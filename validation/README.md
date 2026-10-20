# Offline validation

No school API accounts, teams, tokens, or grade records were created for this validation.
All scores and tokens in tests are synthetic; HTTP calls are mocked.

- Run `mvn test`: upstream average-grade tests, statistics boundary tests, and team API response/request/error tests.
- To exercise the real Swing buttons with a fake database:
  1. `mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt`
  2. Compile `validation/GuiLabCheck.java` with `target/classes` and that dependency classpath.
  3. Run `GuiLabCheck` with `target/test-classes`, `target/classes`, and the dependency classpath on a graphical desktop.
  Expected dialogs: Average Grade 83.0 and Top Grade 85.0 (synthetic members scored 81 and 85).

The GUI check closes only the dialogs it creates. Live connectivity and real teammate collaboration remain unverified.
