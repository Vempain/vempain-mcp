package fi.poltsi.vempain.mcp.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class BearerTokenFilterUTC {

    private static final String TOKEN = "unit-test-token";

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void validBearerTokenContinuesWithAuthenticatedClient() throws Exception {
        var filter = new BearerTokenFilter(sha256(TOKEN));
        var request = mcpRequest();
        request.addHeader("Authorization", "Bearer " + TOKEN);
        var response = new MockHttpServletResponse();
        var chainReached = new AtomicBoolean();
        FilterChain chain = (servletRequest, servletResponse) -> chainReached.set(true);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(chainReached).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull().satisfies(authentication ->
                assertThat(authentication.getName()).isEqualTo("mcp-client"));
    }

    @Test
    void missingOrInvalidBearerTokenIsRejected() throws Exception {
        var filter = new BearerTokenFilter(sha256(TOKEN));
        var request = mcpRequest();
        request.addHeader("Authorization", "Bearer wrong-token");
        var response = new MockHttpServletResponse();
        var chainReached = new AtomicBoolean();
        FilterChain chain = (servletRequest, servletResponse) -> chainReached.set(true);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(chainReached).isFalse();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private static MockHttpServletRequest mcpRequest() {
        var request = new MockHttpServletRequest("POST", "/mcp");
        request.setServletPath("/mcp");
        return request;
    }

    private static String sha256(String value) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
    }
}
