package fi.poltsi.vempain.mcp.client;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class BackendClientConfig {

    @Bean
    RestClient adminBackendRestClient(@Value("${vempain.mcp.admin.url:}") String url,
                                      @Value("${vempain.mcp.admin.api-token:}") String token,
                                      @Value("${vempain.mcp.admin.certificate:}") String certificate) {
        return createClient(url, token, certificate);
    }

    @Bean
    RestClient fileBackendRestClient(@Value("${vempain.mcp.file.url:}") String url,
                                     @Value("${vempain.mcp.file.api-token:}") String token,
                                     @Value("${vempain.mcp.file.certificate:}") String certificate) {
        return createClient(url, token, certificate);
    }

    private RestClient createClient(String url, String token, String certificate) {
        var builder = RestClient.builder().baseUrl(url);
        if (!token.isBlank()) {
            builder.defaultHeader("X-Vempain-Api-Token", token);
        }
        if (!certificate.isBlank()) {
            builder.requestFactory(new JdkClientHttpRequestFactory(httpClient(certificate)));
        }
        return builder.build();
    }

    private HttpClient httpClient(String certificatePath) {
        try {
            var certificateFactory = CertificateFactory.getInstance("X.509");
            var certificate = Files.newInputStream(Path.of(certificatePath));
            try (InputStream input = certificate) {
                var ca = certificateFactory.generateCertificate(input);
                var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                keyStore.setCertificateEntry("vempain-cluster-ca", ca);
                var trustManagers = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagers.init(keyStore);
                var sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagers.getTrustManagers(), null);
                return HttpClient.newBuilder().sslContext(sslContext).build();
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to load backend CA certificate", exception);
        }
    }
}
