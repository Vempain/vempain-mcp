# vempain-mcp — Agent Guide

## Purpose and architecture

`vempain-mcp` is a read-only Spring Boot MCP server. Its HTTP transport is Spring AI Streamable HTTP at `/mcp`; application traffic listens on `8080` and
actuator health on `8081`. Domain tools call the admin and file backends through dedicated read-only endpoints, while schema and SQL tools use separate
PostgreSQL read-only roles. Do not add mutation, task-submitting, or write-capable tools.

MCP client authentication uses a static bearer token. Only its SHA-256 hex digest is configured as `VEMPAIN_MCP_CLIENT_TOKEN_HASH`; compare digests using
`MessageDigest.isEqual`, never log bearer values, and reject unauthenticated MCP requests. Backend API credentials and database credentials are separate and
must not be exposed to MCP clients.

Database query tools must accept a single guarded `SELECT`/`WITH` statement, use a read-only transaction and statement timeout, and cap results. The database
role is the actual access boundary; SQL validation is an additional guard, not a substitute for read-only credentials.

## Build and tests

Use the repository wrapper and Java 25:

```bash
./gradlew clean test
```

Test suffixes have the workspace meanings: `UTC` for unit tests, `CTC` for HTTP/controller tests, `ITC` for database integration tests, and `JTC` for JSON
contracts. Java compilation enables `-Xlint:deprecation`, `-Xlint:unchecked`, and `-Werror`; deprecated third-party features are replaced rather than
suppressed.

Gradle configuration cache is enabled. Keep task actions independent of live build-model objects and align the Java and Spring Boot versions with the other
Vempain Java repositories. Keep DTO JSON property names in `snake_case` and pass backend JSON through unchanged where the tool contract requires it.

## Deployment and configuration

Production backend calls use HTTPS and trust the mounted cluster CA certificate. Every database URL must use TLS (`sslmode=require`); the production profile
must fail closed if TLS is missing. Do not commit credentials, local environment files, generated build output, or deployment secrets.
