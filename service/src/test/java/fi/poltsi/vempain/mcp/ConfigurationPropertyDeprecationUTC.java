package fi.poltsi.vempain.mcp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fi.poltsi.vempain.mcp.config.DeprecatedConfigurationProperties;
import fi.poltsi.vempain.mcp.config.DeprecatedConfigurationProperties.Finding;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationPropertyDeprecationUTC {

    @Test
    void applicationConfigurationBindsNoDeprecatedSpringBootProperties() throws IOException {
        var files = new ArrayList<Path>();
        files.addAll(DeprecatedConfigurationProperties.configurationFiles(Path.of("src", "main", "resources")));
        files.addAll(DeprecatedConfigurationProperties.configurationFiles(Path.of("src", "test", "resources")));
        assertThat(files).as("application configuration files are discovered from the service module")
                .isNotEmpty();

        var findings = DeprecatedConfigurationProperties.scan(files);

        assertThat(findings).as("deprecated configuration keys: %s", findings.stream().map(Finding::describe).toList())
                .isEmpty();
    }
}
