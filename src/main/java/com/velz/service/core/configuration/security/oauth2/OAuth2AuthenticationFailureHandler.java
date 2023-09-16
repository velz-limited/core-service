package com.velz.service.core.configuration.security.oauth2;

import com.velz.service.core.configuration.helpers.CookiesHelper;
import com.velz.service.core.configuration.security.jwt.JWTHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.velz.service.core.configuration.security.oauth2.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private OAuth2AuthorizationRequestRepository oauth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = CookiesHelper.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue).orElse("/");
        oauth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
        JWTHelper.deleteTokensFromCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
