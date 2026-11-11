package fi.poltsi.vempain.mcp.db;

import java.sql.DriverManager;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import fi.poltsi.vempain.mcp.tools.DatabaseTools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class DatabaseToolsITC {

    @Container
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

    private ReadOnlyDataSourceConfig.DatabaseConnections connections;

    @BeforeEach
    void setUp() {
        try (var connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
             var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS mcp_itc_table (id integer)");
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to prepare test database", exception);
        }
        connections = new ReadOnlyDataSourceConfig().databaseConnections(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword(),
                "", "", "", "", "", "", 5, 100);
    }

    @AfterEach
    void tearDown() {
        var dataSource = connections.require("file").getDataSource();
        if (dataSource instanceof AutoCloseable closeable) {
            try {
                closeable.close();
            } catch (Exception exception) {
                throw new IllegalStateException("Unable to close test datasource", exception);
            }
        }
    }

    @Test
    void listsAndDescribesTables() {
        var tools = new DatabaseTools(new DatabaseCatalogService(connections), new SqlGuard());

        assertThat(tools.listTables("file")).anyMatch(row -> "mcp_itc_table".equals(row.get("table_name")));
        assertThat(tools.describeTable("file", "public", "mcp_itc_table"))
                .singleElement()
                .extracting(row -> row.get("column_name"))
                .isEqualTo("id");
    }

    @Test
    void queryReturnsRowsAndRejectsWrites() {
        var tools = new DatabaseTools(new DatabaseCatalogService(connections), new SqlGuard());

        assertThat(tools.query("file", "SELECT 42 AS answer")).containsExactly(Map.of("answer", 42));
        assertThatThrownBy(() -> tools.query("file", "INSERT INTO mcp_itc_table VALUES (1)"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> connections.require("file").execute("INSERT INTO mcp_itc_table VALUES (1)"))
                .hasMessageContaining("read-only");
        assertThat(connections.require("file").queryForObject("SELECT COUNT(*) FROM mcp_itc_table", Integer.class)).isZero();
    }
}
