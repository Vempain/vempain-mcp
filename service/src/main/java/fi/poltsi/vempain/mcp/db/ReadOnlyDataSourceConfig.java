package fi.poltsi.vempain.mcp.db;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ReadOnlyDataSourceConfig {

    @Bean
    DatabaseConnections databaseConnections(
            @Value("${spring.datasource.file.url:}") String fileUrl,
            @Value("${spring.datasource.file.username:}") String fileUsername,
            @Value("${spring.datasource.file.password:}") String filePassword,
            @Value("${spring.datasource.admin.url:}") String adminUrl,
            @Value("${spring.datasource.admin.username:}") String adminUsername,
            @Value("${spring.datasource.admin.password:}") String adminPassword,
            @Value("${spring.datasource.site.url:}") String siteUrl,
            @Value("${spring.datasource.site.username:}") String siteUsername,
            @Value("${spring.datasource.site.password:}") String sitePassword,
            @Value("${vempain.mcp.query.timeout-seconds:30}") int timeoutSeconds,
            @Value("${vempain.mcp.query.max-rows:1000}") int maxRows) {
        var templates = new LinkedHashMap<String, JdbcTemplate>();
        add(templates, "file", fileUrl, fileUsername, filePassword, timeoutSeconds, maxRows);
        add(templates, "admin", adminUrl, adminUsername, adminPassword, timeoutSeconds, maxRows);
        add(templates, "site", siteUrl, siteUsername, sitePassword, timeoutSeconds, maxRows);
        return new DatabaseConnections(templates);
    }

    @Bean
    @Profile("prod")
    DatabaseTlsConfiguration databaseTlsConfiguration(
            @Value("${spring.datasource.file.url:}") String fileUrl,
            @Value("${spring.datasource.admin.url:}") String adminUrl,
            @Value("${spring.datasource.site.url:}") String siteUrl) {
        validateTls("file", fileUrl);
        validateTls("admin", adminUrl);
        validateTls("site", siteUrl);
        return new DatabaseTlsConfiguration();
    }

    private static void validateTls(String database, String url) {
        if (url.isBlank() || !url.matches("(?i).*([?&])sslmode=(require|verify-ca|verify-full)(&|$).*")) {
            throw new IllegalStateException("Production database URL for " + database + " must use sslmode=require, verify-ca or verify-full");
        }
    }

    private void add(Map<String, JdbcTemplate> templates, String name, String url, String username, String password, int timeoutSeconds, int maxRows) {
        if (url.isBlank()) {
            return;
        }
        var dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(url)
                .username(username)
                .password(password)
                .build();
        dataSource.setReadOnly(true);
        dataSource.setConnectionInitSql("SET default_transaction_read_only = on; SET statement_timeout = "
                + timeoutSeconds * 1000L + "; SET lock_timeout = " + timeoutSeconds * 1000L);
        dataSource.setMaximumPoolSize(3);
        var template = new JdbcTemplate(dataSource);
        template.setQueryTimeout(timeoutSeconds);
        template.setMaxRows(maxRows);
        templates.put(name, template);
    }

    public record DatabaseConnections(Map<String, JdbcTemplate> templates) {

        public JdbcTemplate require(String name) {
            var template = templates.get(name);
            if (template == null) {
                throw new IllegalArgumentException("Unknown or unconfigured database: " + name);
            }
            return template;
        }
    }

    static final class DatabaseTlsConfiguration {
    }
}
