package fi.poltsi.vempain.mcp.tools;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import fi.poltsi.vempain.mcp.client.FileBackendClient;
import tools.jackson.databind.JsonNode;

@Component
public class FileBackendTools {

    private final FileBackendClient client;

    public FileBackendTools(FileBackendClient client) {
        this.client = client;
    }

    @McpTool(name = "file_list_files", description = "List files from the file backend.")
    public JsonNode listFiles(@McpToolParam(description = "Zero-based page") int page,
                              @McpToolParam(description = "Page size, from 1 to 500") int size,
                              @McpToolParam(description = "Optional file type") String type,
                              @McpToolParam(description = "Optional name or path search") String search) {
        return client.getFiles(ToolValidation.page(page), ToolValidation.size(size), type, search);
    }

    @McpTool(name = "file_get_file", description = "Get one file and its metadata.")
    public JsonNode getFile(@McpToolParam(description = "File ID") long id) {
        return client.getFile(id);
    }

    @McpTool(name = "file_list_groups", description = "List file groups.")
    public JsonNode listGroups(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getFileGroups(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "file_get_group", description = "Get one file group and its files.")
    public JsonNode getGroup(@McpToolParam(description = "File group ID") long id) {
        return client.getFileGroup(id);
    }

    @McpTool(name = "file_list_tags", description = "List file tags.")
    public JsonNode listTags(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getTags(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "file_list_locations", description = "List GPS locations.")
    public JsonNode listLocations(@McpToolParam(description = "Zero-based page") int page,
                                  @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getLocations(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "file_statistics", description = "Get file backend statistics.")
    public JsonNode statistics() {
        return client.getStatistics();
    }

    @McpTool(name = "file_list_users", description = "List file backend users.")
    public JsonNode listUsers(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getUsers(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "file_list_units", description = "List file backend units.")
    public JsonNode listUnits(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getUnits(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "file_list_acls", description = "List file backend ACL rows.")
    public JsonNode listAcls(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getAcls(ToolValidation.page(page), ToolValidation.size(size));
    }
}
