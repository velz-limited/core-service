package com.velz.service.core._base.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public final class RequestHelper {

    private static final String AUTHORIZATION_BEARER_TEXT = "Bearer ";

    private RequestHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getBearerToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (StringUtils.isNotEmpty(bearer) && bearer.toLowerCase().startsWith(AUTHORIZATION_BEARER_TEXT.toLowerCase())) {
            return bearer.substring(AUTHORIZATION_BEARER_TEXT.length());
        }

        return StringUtils.EMPTY;
    }
}
