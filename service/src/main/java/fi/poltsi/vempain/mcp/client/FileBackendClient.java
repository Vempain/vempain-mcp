package fi.poltsi.vempain.mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;

@Component
public class FileBackendClient {

    private final RestClient client;

    public FileBackendClient(RestClient fileBackendRestClient) {
        client = fileBackendRestClient;
    }

    public JsonNode getFiles(int page, int size, String type, String search) {
        return get("/mcp/files?page=%d&size=%d%s%s".formatted(page, size, optional("type", type), optional("search", search)));
    }

    public JsonNode getFile(long id) {
        return get("/mcp/files/" + id);
    }

    public JsonNode getFileGroups(int page, int size) {
        return get("/mcp/file-groups?page=%d&size=%d".formatted(page, size));
    }

    public JsonNode getFileGroup(long id) {
        return get("/mcp/file-groups/" + id);
    }

    public JsonNode getTags(int page, int size) {
        return get("/mcp/tags?page=%d&size=%d".formatted(page, size));
    }

    public JsonNode getLocations(int page, int size) {
        return get("/mcp/locations?page=%d&size=%d".formatted(page, size));
    }

    public JsonNode getStatistics() {
        return get("/mcp/statistics");
    }

    public JsonNode getUsers(int page, int size) {
        return get("/mcp/users?page=%d&size=%d".formatted(page, size));
    }

    public JsonNode getUnits(int page, int size) {
        return get("/mcp/units?page=%d&size=%d".formatted(page, size));
    }

    public JsonNode getAcls(int page, int size) {
        return get("/mcp/acls?page=%d&size=%d".formatted(page, size));
    }

    private JsonNode get(String path) {
        return BackendClientSupport.get(client, path);
    }

    private String optional(String name, String value) {
        return value == null || value.isBlank() ? "" : "&" + name + "=" + encode(value);
    }

    private String encode(String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
}
