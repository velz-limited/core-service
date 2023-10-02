package com.velz.service.core._base.helpers;

import com.velz.service.core._base.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.velz.service.core._base.helpers.ApplicationContextHelper.getApplicationContext;

public class CookiesHelper {

    private CookiesHelper() {
        throw new UnsupportedOperationException();
    }

    private static AppProperties getAppProperties() {
        return getApplicationContext().getBean(AppProperties.class);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Duration maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge((int) maxAge.toSeconds());
        setSecure(cookie);
        setDomain(cookie, request.getServerName());
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Duration maxAge) {
        addCookie(request, response, name, value, maxAge, true);
    }

    public static void addCookieNoExpiry(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        addCookie(request, response, name, value, Duration.ofSeconds(Integer.MAX_VALUE));
    }

    public static void addCookieNoHttpOnly(HttpServletRequest request, HttpServletResponse response, String name, String value, Duration maxAge) {
        addCookie(request, response, name, value, maxAge, false);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    setSecure(cookie);
                    setDomain(cookie, request.getServerName());
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    private static void setSecure(Cookie cookie) {
        cookie.setSecure(getAppProperties().getCookies().getSecure());
    }

    private static void setDomain(Cookie cookie, String serverName) {
        if (Boolean.TRUE.equals(getAppProperties().getCookies().getStoreRoot())) {
            // Set cookie on domain name level by default.
            List<String> split = List.of(serverName.split("\\."));
            if (split.size() == 1) {
                cookie.setDomain(split.get(0));
            } else if (split.size() > 1) {
                List<String> lastTwo = split.subList(split.size() - 2, split.size());
                String combined = StringUtils.join(lastTwo, ".");
                cookie.setDomain(combined);
            }
        }
    }
}
