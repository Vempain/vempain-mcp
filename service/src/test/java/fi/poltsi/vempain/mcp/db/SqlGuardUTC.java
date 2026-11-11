package fi.poltsi.vempain.mcp.db;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SqlGuardUTC {

    private final SqlGuard guard = new SqlGuard();

    @Test
    void acceptsSelectAndWithQueriesAfterComments() {
        assertThat(guard.validate("-- comment\n SELECT 1")).isEqualTo("SELECT 1");
        assertThat(guard.validate("/* comment */ WITH rows AS (SELECT 1) SELECT * FROM rows")).startsWith("WITH");
    }

    @Test
    void rejectsMutationAndChainedQueries() {
        for (String sql : new String[]{"INSERT INTO users VALUES (1)", "SELECT 1; SELECT 2", "SELECT 1; DROP TABLE users",
                "SELECT set_config('x', 'y', false)", "VACUUM"}) {
            assertThatThrownBy(() -> guard.validate(sql))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void rejectsBlankAndNonQueryStatements() {
        assertThatThrownBy(() -> guard.validate("  ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> guard.validate("EXPLAIN SELECT 1")).isInstanceOf(IllegalArgumentException.class);
    }
}
