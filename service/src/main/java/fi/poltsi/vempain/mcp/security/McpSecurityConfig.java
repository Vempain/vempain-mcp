package fi.poltsi.vempain.mcp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class McpSecurityConfig {

    @Bean
    SecurityFilterChain mcpSecurityFilterChain(HttpSecurity http, @Value("${vempain.mcp.client-token-hash}") String clientTokenHash)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers("/mcp", "/mcp/**").authenticated()
                        .anyRequest().denyAll())
                .addFilterBefore(new BearerTokenFilter(clientTokenHash), AnonymousAuthenticationFilter.class);
        return http.build();
    }
}
