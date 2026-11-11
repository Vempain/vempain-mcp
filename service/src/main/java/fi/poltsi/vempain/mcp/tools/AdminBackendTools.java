package fi.poltsi.vempain.mcp.tools;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import fi.poltsi.vempain.mcp.client.AdminBackendClient;
import tools.jackson.databind.JsonNode;

@Component
public class AdminBackendTools {

    private final AdminBackendClient client;

    public AdminBackendTools(AdminBackendClient client) {
        this.client = client;
    }

    @McpTool(name = "admin_list_pages", description = "List admin pages.")
    public JsonNode listPages(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getPages(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_get_page", description = "Get one admin page.")
    public JsonNode getPage(@McpToolParam(description = "Page ID") long id) {
        return client.getPage(id);
    }

    @McpTool(name = "admin_list_galleries", description = "List galleries.")
    public JsonNode listGalleries(@McpToolParam(description = "Zero-based page") int page,
                                  @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getGalleries(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_get_gallery", description = "Get one gallery.")
    public JsonNode getGallery(@McpToolParam(description = "Gallery ID") long id) {
        return client.getGallery(id);
    }

    @McpTool(name = "admin_list_site_files", description = "List published site files.")
    public JsonNode listSiteFiles(@McpToolParam(description = "Zero-based page") int page,
                                  @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getSiteFiles(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_data_sets", description = "List data sets.")
    public JsonNode listDataSets(@McpToolParam(description = "Zero-based page") int page,
                                 @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getDataSets(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_layouts", description = "List layouts.")
    public JsonNode listLayouts(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getLayouts(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_components", description = "List components.")
    public JsonNode listComponents(@McpToolParam(description = "Zero-based page") int page,
                                   @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getComponents(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_forms", description = "List forms.")
    public JsonNode listForms(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getForms(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_schedules", description = "List publishing schedules.")
    public JsonNode listSchedules(@McpToolParam(description = "Zero-based page") int page,
                                  @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getSchedules(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_tags", description = "List admin tags.")
    public JsonNode listTags(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getTags(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_users", description = "List admin users.")
    public JsonNode listUsers(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getUsers(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_units", description = "List admin units.")
    public JsonNode listUnits(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getUnits(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_acls", description = "List admin ACL rows.")
    public JsonNode listAcls(@McpToolParam(description = "Zero-based page") int page, @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getAcls(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_list_published_pages", description = "List published website pages.")
    public JsonNode listPublishedPages(@McpToolParam(description = "Zero-based page") int page,
                                       @McpToolParam(description = "Page size, from 1 to 500") int size) {
        return client.getPublishedPages(ToolValidation.page(page), ToolValidation.size(size));
    }

    @McpTool(name = "admin_statistics", description = "Get admin backend statistics.")
    public JsonNode statistics() {
        return client.getStatistics();
    }
}
