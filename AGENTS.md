**Quick Start**
- Build: `mvn -DskipTests package`
- Run (dev): `mvn spring-boot:run`
- Run (jar): `java -jar target/ZiSnackDesk-0.0.1-SNAPSHOT.jar`
- Test: `mvn test`
- Docker: `docker build -t zisnackdesk .` and run with required environment variables.

**Environment Variables**
- Required: `REDIS_HOST`, `REDIS_PORT`, `REDIS_PWD`, `PG_URL`, `PG_USER`, `PG_PWD`

**Key Files & Entry Points**
- Project POM: [pom.xml](pom.xml)
- Spring configuration: [src/main/resources/application.yml](src/main/resources/application.yml)
- Main class: [src/main/java/com/bosyon/zisnackdesk/ZiSnackDeskApplication.java](src/main/java/com/bosyon/zisnackdesk/ZiSnackDeskApplication.java)
- Configuration classes: [src/main/java/com/bosyon/zisnackdesk/config/MyBatisPlusConfig.java](src/main/java/com/bosyon/zisnackdesk/config/MyBatisPlusConfig.java), [src/main/java/com/bosyon/zisnackdesk/config/RedisConfig.java](src/main/java/com/bosyon/zisnackdesk/config/RedisConfig.java)
- Controllers / API: [src/main/java/com/bosyon/zisnackdesk/controller](src/main/java/com/bosyon/zisnackdesk/controller)
- MyBatis mappers: [src/main/java/com/bosyon/zisnackdesk/mapper](src/main/java/com/bosyon/zisnackdesk/mapper)
- Domain models / DTOs / VOs: [src/main/java/com/bosyon/zisnackdesk/model](src/main/java/com/bosyon/zisnackdesk/model)
- Services: [src/main/java/com/bosyon/zisnackdesk/service](src/main/java/com/bosyon/zisnackdesk/service)
- Tests: [test](test)

**Tech Stack & Conventions**
- Java: 21
- Framework: Spring Boot 4
- ORM: MyBatis-Plus (mybatis-plus-spring-boot4-starter)
- Logging: Logback (configured in `application.yml`)
- Lombok: used; enable annotation processing in your IDE
- Database: PostgreSQL (runtime dependency)
- Redis: Lettuce client (configured in `application.yml`)
- Package layout: follow `com.bosyon.zisnackdesk` root; keep controllers, services, mappers, and models separated

**Agent Guidance (Behavior Guidelines)**
- Link, don't embed: reference repository docs and source files (see links above); avoid copying large business documentation into instruction files.
- Follow the brainstorming skill HARD-GATE: propose a design and obtain user/maintainer approval before making implementation changes.
- Ask one question at a time; prefer multiple-choice options to speed responses.
- Run and verify: before claiming changes pass tests, run `mvn test` locally (or equivalent) and report results.
- Commit strategy: do not push breaking changes directly to the default branch; create a feature branch with clear commit messages and a PR description.

**Quick Checks for Contributors / Agents**
- Fast local run: set required environment variables, then:

```bash
mvn -DskipTests package
mvn test
```

- When interacting with DB/Redis, prefer containerized services or mock configurations to avoid impacting production systems.

**Suggested Next Customizations**
- Add `.github/copilot-instructions.md` to mirror key points for GitHub Copilot and PR workflows.
- Add a small agent skill file to automate common checks (environment variable presence, running `mvn test`, etc.).
- Add repository rules: [.agents/rules/git-commit-message.md](.agents/rules/git-commit-message.md) — commit message guideline.

---
Table of changed files:

- `AGENTS.md` — Created: contains quick start commands, key paths, and behavior guidance for AI agents and new contributors.
