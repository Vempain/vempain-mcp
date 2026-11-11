package fi.poltsi.vempain.mcp.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public final class BearerTokenFilter extends OncePerRequestFilter {

    private static final String MCP_PATH = "/mcp";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_SCHEME = "Bearer ";
    private static final String HASH_PATTERN = "[0-9a-fA-F]{64}";

    private final byte[] expectedTokenHash;

    public BearerTokenFilter(String configuredTokenHash) {
        if (configuredTokenHash == null || !configuredTokenHash.matches(HASH_PATTERN)) {
            throw new IllegalArgumentException("vempain.mcp.client-token-hash must be a 64-character SHA-256 hex digest");
        }
        expectedTokenHash = HexFormat.of().parseHex(configuredTokenHash);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var path = request.getServletPath();
        return !MCP_PATH.equals(path) && !path.startsWith(MCP_PATH + "/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = bearerToken(request.getHeader(AUTHORIZATION_HEADER));
        if (token == null || !MessageDigest.isEqual(expectedTokenHash, hash(token))) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated("mcp-client", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private static String bearerToken(String authorization) {
        if (authorization == null || !authorization.regionMatches(true, 0, BEARER_SCHEME, 0, BEARER_SCHEME.length())) {
            return null;
        }
        var token = authorization.substring(BEARER_SCHEME.length()).trim();
        return token.isEmpty() ? null : token;
    }

    private static byte[] hash(String token) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
