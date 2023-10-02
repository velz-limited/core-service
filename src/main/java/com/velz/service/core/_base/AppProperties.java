package com.velz.service.core._base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private Cors cors = new Cors();
    private Cookies cookies = new Cookies();
    private Jwt jwt = new Jwt();
    private OAuth2 oauth2 = new OAuth2();

    @Data
    public static class Cors {
        private Boolean allowAllOrigins = false;
        private List<String> allowOrigins = new ArrayList<>();
        private Boolean allowCredentials = true;
        private Duration maxAge = Duration.ofMinutes(10);
    }

    @Data
    public static class Cookies {
        private Boolean storeRoot = true;
        private Boolean secure = true;
    }

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

    @Data
    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}
