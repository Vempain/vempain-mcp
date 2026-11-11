package fi.poltsi.vempain.mcp.tools;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fi.poltsi.vempain.mcp.client.AdminBackendClient;
import tools.jackson.databind.node.JsonNodeFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminBackendToolsUTC {

    @Test
    void delegatesPagedResourceReadsToAdminClient() {
        var client = Mockito.mock(AdminBackendClient.class);
        var tools = new AdminBackendTools(client);
        var response = JsonNodeFactory.instance.objectNode();
        when(client.getPublishedPages(1, 50)).thenReturn(response);

        assertThat(tools.listPublishedPages(1, 50)).isSameAs(response);

        verify(client).getPublishedPages(1, 50);
    }
}
