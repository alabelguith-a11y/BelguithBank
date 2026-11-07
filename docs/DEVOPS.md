# BelguithBank: DevOps notes

This repo follows the **TinyLink Production Readiness Challenge** milestones (M1–M10) from the `devops-capstone-coach` skill.
Same pipeline, different app: Git workflow → Docker → CI → Kubernetes → GitOps → progressive delivery → observability → policy → IaC → game day.

## 1. What was changed in the code (and why)

| Change | Why it is needed for containers / Kubernetes |
|---|---|
| `pom.xml`: `war` → `jar`, Tomcat no longer `provided` | A container runs the embedded Tomcat. A WAR is for dropping into an external Tomcat. |
| Deleted `ServletInitializer` | Only used for WAR deployment to an external server. |
| `<finalName>belguithbank</finalName>` | Stable jar name (`target/belguithbank.jar`) for the Dockerfile and CI. |
| `application.properties` reads `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DDL_AUTO`, `PORT` | Config comes from the environment (ConfigMap/Secret), never baked into the image. Defaults keep local dev unchanged. |
| Removed hard-coded JDBC driver and Hibernate dialect | Auto-detected from the URL; lets tests run on H2. |
| `server.shutdown=graceful` | In-flight requests finish on SIGTERM, needed for zero-downtime rollouts and node drains. |
| Actuator + Prometheus registry on **port 8081** | Liveness/readiness probes and `/actuator/prometheus` metrics, kept off the public port. Readiness includes the DB; liveness does not. |
| `application-test.properties` (H2) + `@ActiveProfiles("test")` | `contextLoads` used to need a real MySQL, so CI would fail. |
| `mvnw` made executable (`100755`) | It was committed as `100644`; `./mvnw` fails with "Permission denied" on Linux runners. |
| `.gitignore` + `.env.example` | Keep local secrets out of git. |
| `Dockerfile`, `docker-compose.yml`, `.github/workflows/ci.yml` | **Deliberately naive starters.** Improving them is milestones M1/M2. |

## 2. Run it locally

```bash
./mvnw -B package                      # tests run on H2, no MySQL needed
cp .env.example .env                   # then edit the passwords
docker compose up --build
curl -s localhost:8081/actuator/health/liveness
curl -s localhost:8081/actuator/health/readiness
curl -s localhost:8081/actuator/prometheus | head
open http://localhost:8080/clients
```

## 3. Findings from reviewing the code (NOT changed: your call)

| Severity | Finding | Why it matters | Natural home |
|---|---|---|---|
| **major** | No authentication, authorization or CSRF protection (no Spring Security). Anyone who can reach the app can read, edit and delete clients and accounts. | It is a banking domain. Also fails any real security review. | M8 / Security track: Spring Security (form login or OIDC with Keycloak in-cluster), CSRF tokens in Thymeleaf forms |
| **major** | `Compte.solde` is a `float`. | Binary floating point cannot represent money exactly (0.1 + 0.2 problem). | M7: migrate to `BigDecimal` / `DECIMAL(19,2)` as a real **expand/contract** migration (add column, dual-write, backfill in batches, switch reads, drop old) while k6 runs |
| **major** | `ddl-auto=update` creates/alters tables at app start. With several replicas, they race; schema changes are invisible in git. | Unreviewable schema drift in production. | M7: Flyway/Liquibase migration run as a Job or Argo PreSync hook, then `DDL_AUTO=validate` |
| minor | No bean validation (`@Valid`) or global error handling (`@ControllerAdvice`); deleting a client that still has accounts will likely surface as a 500. | Noisy 5xx burn your error budget for user mistakes. | M6 (SLO hygiene) |
| minor | `findAll()` without pagination; `open-in-view` left at its default. | Latency and DB connection holding as data grows. | M6 |
| nit | `FirstSpringBootApplication` has an empty `CommandLineRunner` and unused injected fields. | Dead code. | any PR |

## 4. Spring-Boot-specific things to get right per milestone (pointers, not solutions)

- **M1 images:** a JRE image (not JDK), layered jar extraction, non-root *numeric* UID, container-aware JVM flags (`-XX:MaxRAMPercentage`), digest pinning. Write an ADR: Dockerfile vs Buildpacks (`spring-boot:build-image`).
- **M2 CI:** swap H2 for Testcontainers MySQL for fidelity (hard mode), add coverage gate, SBOM, sign by digest.
- **M3 Kubernetes:**
  - Probes hit port **8081**: `startupProbe` (JVMs start slowly), `livenessProbe` → `/actuator/health/liveness`, `readinessProbe` → `/actuator/health/readiness`.
  - `terminationGracePeriodSeconds` must exceed `spring.lifecycle.timeout-per-shutdown-phase` (20 s) plus any `preStop` delay.
  - Memory limit ≥ heap + metaspace + threads + native; size from measurements, not guesses.
  - Hikari pool size × replicas must stay under MySQL `max_connections`.
  - MySQL: operator vs StatefulSet is an ADR decision (PVCs, backups).
  - NetworkPolicy: ingress-nginx → 8080 only, Prometheus → 8081 only, app → MySQL 3306 + DNS.
  - Config: `DB_URL` in a ConfigMap, `DB_PASSWORD` from a sealed/external secret.
- **M4 GitOps:** promote by image digest; migration Job as a PreSync hook.
- **M5/M6 observability:** Micrometer exposes `http_server_requests_seconds_*`, `jvm_*`, `hikaricp_*`. Build SLOs on `http_server_requests_seconds_count` by `status` (never label by raw URI with path variables; keep cardinality low).
- **M8 policy:** admission policies plus the Spring Security work above.
- **M10 game day:** `scripts/roulette.sh` scenarios `bad-redis-url` and `click-storm` do not apply (no Redis here); the others do. Adapt the names with env vars.

## 5. Proof checklist for the first milestone

- [ ] `./mvnw -B verify` is green locally and in CI (first run is the real verification).
- [ ] `docker compose up` serves `/clients`, and `/actuator/health/readiness` returns `UP`.
- [ ] Stopping MySQL makes **readiness** go `DOWN` while **liveness** stays `UP`.
- [ ] `docker run --rm --entrypoint id <image>` shows a non-root user (after you fix the Dockerfile).
