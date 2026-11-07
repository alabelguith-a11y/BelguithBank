# STARTER: works, but deliberately naive. Milestone M1 is to improve it:
# multi-stage build (build inside Docker, not on the host), JRE-only runtime, pinned digest,
# non-root numeric user, layered jar (java -Djarmode=tools extract --layers), container-aware JVM flags,
# healthcheck against the actuator liveness endpoint.
#
# Build the jar first on the host:  ./mvnw -B package
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY target/belguithbank.jar app.jar
EXPOSE 8080 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
