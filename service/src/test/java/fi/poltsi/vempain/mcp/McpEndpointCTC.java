package fi.poltsi.vempain.mcp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "management.server.port=0")
class McpEndpointCTC {

    private static final String TOKEN = "mcp-endpoint-ctc-token";

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void clientTokenHash(DynamicPropertyRegistry registry) {
        registry.add("vempain.mcp.client-token-hash", McpEndpointCTC::sha256Token);
    }

    @Test
    void initializeIsAvailableAtStreamableEndpointWithBearerToken() throws Exception {
        var body = """
                {"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-06-18","capabilities":{},"clientInfo":{"name":"ctc","version":"1.0"}}}
                """;
        var response = send(body, "Bearer " + TOKEN);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"result\"").contains("\"serverInfo\"");
    }

    @Test
    void initializeWithoutBearerTokenIsUnauthorized() throws Exception {
        var response = send("{}", null);

        assertThat(response.statusCode()).isEqualTo(401);
    }

    private HttpResponse<String> send(String body, String authorization) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/mcp"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (authorization != null) {
            builder.header("Authorization", authorization);
        }
        return HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static String sha256Token() {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(TOKEN.getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
