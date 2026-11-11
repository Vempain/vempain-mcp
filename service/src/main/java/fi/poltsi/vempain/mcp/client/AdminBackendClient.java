package fi.poltsi.vempain.mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;

@Component
public class AdminBackendClient {

    private final RestClient client;

    public AdminBackendClient(RestClient adminBackendRestClient) {
        client = adminBackendRestClient;
    }

    public JsonNode getPages(int page, int size) {
        return getPaged("pages", page, size);
    }

    public JsonNode getPage(long id) {
        return get("/mcp/pages/" + id);
    }

    public JsonNode getGalleries(int page, int size) {
        return getPaged("galleries", page, size);
    }

    public JsonNode getGallery(long id) {
        return get("/mcp/galleries/" + id);
    }

    public JsonNode getSiteFiles(int page, int size) {
        return getPaged("site-files", page, size);
    }

    public JsonNode getDataSets(int page, int size) {
        return getPaged("data-sets", page, size);
    }

    public JsonNode getLayouts(int page, int size) {
        return getPaged("layouts", page, size);
    }

    public JsonNode getComponents(int page, int size) {
        return getPaged("components", page, size);
    }

    public JsonNode getForms(int page, int size) {
        return getPaged("forms", page, size);
    }

    public JsonNode getSchedules(int page, int size) {
        return getPaged("schedules", page, size);
    }

    public JsonNode getTags(int page, int size) {
        return getPaged("tags", page, size);
    }

    public JsonNode getUsers(int page, int size) {
        return getPaged("users", page, size);
    }

    public JsonNode getUnits(int page, int size) {
        return getPaged("units", page, size);
    }

    public JsonNode getAcls(int page, int size) {
        return getPaged("acls", page, size);
    }

    public JsonNode getPublishedPages(int page, int size) {
        return getPaged("web-site/pages", page, size);
    }

    public JsonNode getStatistics() {
        return get("/mcp/statistics");
    }

    private JsonNode getPaged(String resource, int page, int size) {
        return get("/mcp/" + resource + "?page=%d&size=%d".formatted(page, size));
    }

    private JsonNode get(String path) {
        return BackendClientSupport.get(client, path);
    }
}
