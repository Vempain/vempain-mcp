package fi.poltsi.vempain.mcp.tools;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fi.poltsi.vempain.mcp.client.FileBackendClient;
import tools.jackson.databind.node.JsonNodeFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileBackendToolsUTC {

    private final FileBackendClient client = Mockito.mock(FileBackendClient.class);
    private final FileBackendTools tools = new FileBackendTools(client);

    @Test
    void delegatesPagingAndFiltersToFileClient() {
        var response = JsonNodeFactory.instance.objectNode();
        when(client.getFiles(2, 25, "IMAGE", "lake")).thenReturn(response);

        assertThat(tools.listFiles(2, 25, "IMAGE", "lake")).isSameAs(response);

        verify(client).getFiles(2, 25, "IMAGE", "lake");
    }

    @Test
    void rejectsInvalidPageSizeBeforeCallingBackend() {
        assertThatThrownBy(() -> tools.listGroups(0, 501))
                .isInstanceOf(IllegalArgumentException.class);
        Mockito.verifyNoInteractions(client);
    }
}
