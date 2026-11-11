package fi.poltsi.vempain.mcp.db;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class SqlGuard {

    private static final Pattern LEADING_COMMENT = Pattern.compile("^(?:\\s|--[^\\r\\n]*(?:\\r?\\n|$)|/\\*.*?\\*/)*", Pattern.DOTALL);
    private static final Pattern READ_STATEMENT = Pattern.compile("(?is)^(select|with)\\b");
    private static final Pattern FORBIDDEN = Pattern.compile(
            "(?is)\\b(insert|update|delete|merge|drop|alter|create|truncate|grant|revoke|copy|call|set_config|set|reset|vacuum|refresh)\\b");

    public String validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL query must not be blank");
        }
        var normalized = LEADING_COMMENT.matcher(sql).replaceFirst("").trim();
        if (normalized.indexOf(';') >= 0 || !READ_STATEMENT.matcher(normalized).find() || FORBIDDEN.matcher(normalized).find()) {
            throw new IllegalArgumentException("Only a single SELECT or WITH query is allowed");
        }
        return normalized;
    }
}
