package fi.poltsi.vempain.mcp.tools;

import java.util.List;
import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import fi.poltsi.vempain.mcp.db.DatabaseCatalogService;
import fi.poltsi.vempain.mcp.db.SqlGuard;

@Component
public class DatabaseTools {

    private final DatabaseCatalogService catalog;
    private final SqlGuard sqlGuard;

    public DatabaseTools(DatabaseCatalogService catalog, SqlGuard sqlGuard) {
        this.catalog = catalog;
        this.sqlGuard = sqlGuard;
    }

    @McpTool(name = "database_list_tables", description = "List tables in the file, admin or site database.")
    public List<Map<String, Object>> listTables(@McpToolParam(description = "Database name: file, admin or site") String database) {
        return catalog.listTables(database);
    }

    @McpTool(name = "database_describe_table", description = "Describe columns of a database table.")
    public List<Map<String, Object>> describeTable(@McpToolParam(description = "Database name: file, admin or site") String database,
                                                   @McpToolParam(description = "Schema name") String schema,
                                                   @McpToolParam(description = "Table name") String table) {
        return catalog.describeTable(database, schema, table);
    }

    @McpTool(name = "database_query", description = "Run one read-only SELECT or WITH query. Results are capped by server configuration.")
    public List<Map<String, Object>> query(@McpToolParam(description = "Database name: file, admin or site") String database,
                                           @McpToolParam(description = "A single SELECT or WITH statement") String sql) {
        return catalog.query(database, sqlGuard.validate(sql));
    }
}
