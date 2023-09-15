package com.velz.service.core.configuration.security.jwt;

import com.velz.service.core.configuration.AppProperties;
import com.velz.service.core.configuration.helpers.CookiesHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import static com.velz.service.core.configuration.helpers.ApplicationContextHelper.getApplicationContext;

public class JWTHelper {

    public static final String TYPE_CLAIM = "type";
    public static final String ROLE_CLAIM = "role";

    private JWTHelper() {
        throw new UnsupportedOperationException();
    }

    private static AppProperties getAppProperties() {
        return getApplicationContext().getBean(AppProperties.class);
    }

    public static Map<JWTType, String> create(@NonNull JWTUserClaims userClaims) {
        EnumMap<JWTType, String> tokens = new EnumMap<>(JWTType.class);
        tokens.put(JWTType.REFRESH, createSignedToken(JWTType.REFRESH, userClaims, getAppProperties().getJwt().getRefreshToken().getExpires()));
        tokens.put(JWTType.ACCESS, createSignedToken(JWTType.ACCESS, userClaims, getAppProperties().getJwt().getAccessToken().getExpires()));
        return tokens;
    }

    public static Map<JWTType, String> refresh(@NonNull JWTUserClaims userClaims) {
        EnumMap<JWTType, String> tokens = new EnumMap<>(JWTType.class);
        tokens.put(JWTType.ACCESS, createSignedToken(JWTType.ACCESS, userClaims, getAppProperties().getJwt().getAccessToken().getExpires()));
        return tokens;
    }

    public static String createSignedToken(@NonNull JWTType type, @NonNull JWTUserClaims userClaims, @NonNull Duration expires) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expires.toMillis());

        Claims moreClaims = Jwts.claims();
        moreClaims.put(TYPE_CLAIM, type.name().toLowerCase());
        moreClaims.put(ROLE_CLAIM, userClaims.getRoleString().toLowerCase());

        return Jwts.builder()
                .setClaims(moreClaims)
                .setIssuer(getAppProperties().getJwt().getIssuer())
                .setSubject(userClaims.getIdString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getAppProperties().getJwt().getRsaPrivateKey())
                .compact();
    }

    public static Jws<Claims> parseClaims(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getAppProperties().getJwt().getRsaPublicKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validateClaims(JWTType type, Jws<Claims> claims) {
        // Check type and claims not null.
        if (type == null || claims == null) {
            return false;
        }

        // Check token type.
        if (!type.name().equalsIgnoreCase(claims.getBody().get(TYPE_CLAIM, String.class))) {
            return false;
        }

        // Check issue and expiration times.
        Date now = new Date();
        Date issuedAt = claims.getBody().getIssuedAt();
        Date expiration = claims.getBody().getExpiration();
        if (issuedAt == null || expiration == null || now.before(issuedAt) || now.after(expiration)) {
            return false;
        }

        // Check subject (principal) not null.
        if (claims.getBody().getSubject() == null) {
            return false;
        }

        return true;
    }

    public static void addTokensToCookies(HttpServletResponse response, Map<JWTType, String> tokens) {
        String refreshToken = tokens.get(JWTType.REFRESH);
        if (StringUtils.isNotEmpty(refreshToken)) {
            AppProperties.Jwt.Transport refreshTokenTransport = getAppProperties().getJwt().getRefreshToken().getTransport();
            if (Boolean.TRUE.equals(refreshTokenTransport.getCookies())) {
                CookiesHelper.addCookieNoExpiry(response, JWTType.REFRESH.getSnakeName(), refreshToken);
            }
        }

        String accessToken = tokens.get(JWTType.ACCESS);
        if (StringUtils.isNotEmpty(accessToken)) {
            AppProperties.Jwt.Transport accessTokenTransport = getAppProperties().getJwt().getAccessToken().getTransport();
            if (Boolean.TRUE.equals(accessTokenTransport.getCookies())) {
                CookiesHelper.addCookieNoExpiry(response, JWTType.ACCESS.getSnakeName(), accessToken);
            }
        }
    }

    public static void addTokensToMapBody(Map<String, Object> map, Map<JWTType, String> tokens) {
        String refreshToken = tokens.get(JWTType.REFRESH);
        if (StringUtils.isNotEmpty(refreshToken)) {
            AppProperties.Jwt.Transport refreshTokenTransport = getAppProperties().getJwt().getRefreshToken().getTransport();
            if (Boolean.TRUE.equals(refreshTokenTransport.getBody())) {
                map.put(JWTType.REFRESH.getSnakeName(), tokens.get(JWTType.REFRESH));
            }
        }

        String accessToken = tokens.get(JWTType.ACCESS);
        if (StringUtils.isNotEmpty(accessToken)) {
            AppProperties.Jwt.Transport accessTokenTransport = getAppProperties().getJwt().getAccessToken().getTransport();
            if (Boolean.TRUE.equals(accessTokenTransport.getBody())) {
                map.put(JWTType.ACCESS.getSnakeName(), tokens.get(JWTType.ACCESS));
            }
        }
    }

    public static void deleteTokensFromCookies(HttpServletRequest request, HttpServletResponse response) {
        CookiesHelper.deleteCookie(request, response, JWTType.ACCESS.getSnakeName());
        CookiesHelper.deleteCookie(request, response, JWTType.REFRESH.getSnakeName());
    }
}
