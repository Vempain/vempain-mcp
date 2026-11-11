package fi.poltsi.vempain.mcp.db;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DatabaseCatalogService {

    private final ReadOnlyDataSourceConfig.DatabaseConnections connections;

    public DatabaseCatalogService(ReadOnlyDataSourceConfig.DatabaseConnections connections) {
        this.connections = connections;
    }

    public List<Map<String, Object>> listTables(String database) {
        return connections.require(database).queryForList("""
                SELECT table_schema, table_name, table_type
                FROM information_schema.tables
                WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
                ORDER BY table_schema, table_name
                """);
    }

    public List<Map<String, Object>> describeTable(String database, String schema, String table) {
        return connections.require(database).queryForList("""
                SELECT ordinal_position, column_name, data_type, is_nullable, column_default
                FROM information_schema.columns
                WHERE table_schema = ? AND table_name = ?
                ORDER BY ordinal_position
                """, schema, table);
    }

    public List<Map<String, Object>> query(String database, String sql) {
        return connections.require(database).queryForList(sql);
    }
}
