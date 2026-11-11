# vempain-mcp

Read-only Model Context Protocol server for Vempain. The server exposes MCP over Streamable HTTP at `/mcp` on port `8080`; health and readiness endpoints are
served on management port `8081`.

The MCP endpoint requires an authorization bearer token. Configure `VEMPAIN_MCP_CLIENT_TOKEN_HASH` with the lowercase or uppercase hexadecimal SHA-256 digest of
the token; the bearer value itself is never configured or logged by this service. The hash must contain exactly 64 hexadecimal characters, and the service
refuses to start with an invalid value.

```bash
printf '%s' 'replace-with-a-long-random-token' | sha256sum
VEMPAIN_MCP_CLIENT_TOKEN_HASH='<64-character-digest>' ./start.sh
```

## Build and run

The build requires JDK 25 and uses the checked-in Gradle 9.8.2 wrapper.

```bash
./gradlew clean test
./start.sh
```

`./start.sh -t` runs the tests before starting the application. The Docker image is runtime-only; CI builds the service jar before building the image.
