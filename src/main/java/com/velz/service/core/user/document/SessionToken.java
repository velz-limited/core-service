package com.velz.service.core.user.document;

import com.velz.service.core.configuration.security.jwt.JWTType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

import static com.velz.service.core.configuration.security.jwt.JWTHelper.parseClaims;
import static com.velz.service.core.configuration.security.jwt.JWTHelper.validateClaims;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@Setter
public class SessionToken {

    @NotBlank
    private String hashedToken;

    @NotNull
    private Date issuedAt;

    @NotNull
    private Date expiresAt;

    // TODO J: Get IP address and/or other client identifiers.

    @SneakyThrows
    public static String hashToken(String token) {
        if (token == null) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static SessionToken buildSessionToken(String token) {
        Jws<Claims> claims = parseClaims(token);
        if (!validateClaims(JWTType.REFRESH, claims)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid refresh token.");
        }
        SessionToken sessionToken = new SessionToken();
        sessionToken.setIssuedAt(claims.getBody().getIssuedAt());
        sessionToken.setExpiresAt(claims.getBody().getExpiration());
        sessionToken.setHashedToken(hashToken(token));
        return sessionToken;
    }
}
