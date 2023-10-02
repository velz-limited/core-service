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
        setDomain(cookie, request.getServerName());
        setSecure(cookie);
        setSameSite(cookie);
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
                    addCookie(request, response, name, "", Duration.ZERO);
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

    private static void setDomain(Cookie cookie, String serverName) {
        int dotLevel = getAppProperties().getCookies().getDomainDotLevel();

        if (dotLevel <= 0) {
            return;
        }

        List<String> split = List.of(serverName.split("\\."));
        if (split.size() == 1) {
            cookie.setDomain(split.get(0));
        } else if (split.size() > 1) {
            List<String> parts = split.subList(Math.max(split.size() - (dotLevel + 1), 0), split.size());
            String combined = StringUtils.join(parts, ".");
            cookie.setDomain(combined);
        }
    }

    private static void setSecure(Cookie cookie) {
        if (!"none".equalsIgnoreCase(getAppProperties().getCookies().getSameSite())) {
            cookie.setSecure(getAppProperties().getCookies().getSecure());
        } else {
            cookie.setSecure(true);
        }
    }

    private static void setSameSite(Cookie cookie) {
        cookie.setAttribute("SameSite", getAppProperties().getCookies().getSameSite());
    }
}
