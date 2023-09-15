package com.velz.service.core.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private RSAPrivateKey rsaPrivateKey;
        private RSAPublicKey rsaPublicKey;
        private String issuer = "velz-core";
        private AccessToken accessToken = new AccessToken();
        private RefreshToken refreshToken = new RefreshToken();

        @Data
        public static class AccessToken {
            private Duration expires = Duration.ofMinutes(5);
            private Transport transport = new Transport();
        }

        @Data
        public static class RefreshToken {
            private Duration expires = Duration.ofHours(8760);
            private Transport transport = new Transport();
        }

        @Data
        public static class Transport {
            private Boolean cookies = true;
            private Boolean body = false;
        }
    }
}
