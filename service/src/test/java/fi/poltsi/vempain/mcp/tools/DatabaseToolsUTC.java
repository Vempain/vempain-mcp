package fi.poltsi.vempain.mcp.tools;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fi.poltsi.vempain.mcp.db.DatabaseCatalogService;
import fi.poltsi.vempain.mcp.db.SqlGuard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseToolsUTC {

    private final DatabaseCatalogService catalog = Mockito.mock(DatabaseCatalogService.class);
    private final DatabaseTools tools = new DatabaseTools(catalog, new SqlGuard());

    @Test
    void delegatesTableOperations() {
        var tables = List.<Map<String, Object>>of(Map.of("table_name", "page"));
        when(catalog.listTables("admin")).thenReturn(tables);

        assertThat(tools.listTables("admin")).isSameAs(tables);
        verify(catalog).listTables("admin");
    }

    @Test
    void validatesQueryBeforeCallingDatabase() {
        assertThatThrownBy(() -> tools.query("file", "UPDATE file SET name = 'x'"))
                .isInstanceOf(IllegalArgumentException.class);
        Mockito.verifyNoInteractions(catalog);
    }
}
