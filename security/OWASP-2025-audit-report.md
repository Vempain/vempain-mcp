# OWASP 2025 audit report

This report tracks the security controls for the read-only MCP server. The first implementation stage establishes the client authentication boundary; backend
token scopes, database read-only roles, the SQL guard, and deployment wiring are completed in later planned stages.

| Area               | Risk and control                                                                                                                                                                                       |
|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Authentication     | MCP requests require a bearer token. Only the SHA-256 digest is configured; the filter compares fixed-size digests with `MessageDigest.isEqual` and does not include credentials in responses or logs. |
| Authorization      | The only intended MCP capabilities are read-only. Backend calls use `MCP_READ` credentials restricted to `GET /mcp/**`; database connections use dedicated read-only roles.                            |
| Injection          | SQL tools will allow one `SELECT`/`WITH` statement and reject mutation statements; PostgreSQL read-only roles and read-only transactions remain the enforcement boundary.                              |
| Transport security | Production backend and database connections use TLS. Database JDBC URLs require `sslmode=require`.                                                                                                     |
| Secret handling    | Client bearer tokens, backend API tokens, and database passwords are environment/secret inputs and must never be logged or committed.                                                                  |

Controls listed as future-stage behavior must be verified before that functionality is exposed in a deployment.
