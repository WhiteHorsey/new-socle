package ma.bcp.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.secret.keys")
public class AppSecretKeysConfigProperties {
    private final List<AppSecretKeys> apps = new ArrayList<>();
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppSecretKeys {
        private String name;
        private String secretKey;
    }
}