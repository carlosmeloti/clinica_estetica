# =============================================================================
# Multi-stage build: Angular (../frontend) + Spring Boot → JRE 21 Alpine
#
# Contexto de build = pasta PAI (clinita-estetica/), que contém:
#   frontend/
#   clinica_estetica/
#
# Exemplos:
#   docker build -f clinica_estetica/Dockerfile -t clinica-app ..
#   docker compose -f clinica_estetica/docker-compose.yml up --build
# =============================================================================

# ---------- Stage 1: Maven + Node (via frontend-maven-plugin) ----------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

# Backend (pom primeiro para cache de dependências Maven)
COPY clinica_estetica/pom.xml clinica_estetica/
COPY clinica_estetica/src clinica_estetica/src

# Front Angular (irmão do backend)
COPY frontend frontend

WORKDIR /workspace/clinica_estetica

# -Pwith-frontend: npm ci + ng build + cópia para target/classes/static
RUN mvn -B -Pwith-frontend clean package -DskipTests \
 && cp target/clinica-*.jar /workspace/app.jar

# ---------- Stage 2: runtime leve ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring \
 && apk add --no-cache curl

COPY --from=build /workspace/app.jar /app/app.jar

USER spring:spring

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -fsS http://localhost:8080/ || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
