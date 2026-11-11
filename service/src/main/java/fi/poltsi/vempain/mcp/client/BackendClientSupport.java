package fi.poltsi.vempain.mcp.client;

import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;

final class BackendClientSupport {

    private BackendClientSupport() {
    }

    static JsonNode get(RestClient client, String path) {
        try {
            return client.get().uri(path).retrieve().body(JsonNode.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Backend read failed", exception);
        }
    }
}
