package com.velz.service.core.configuration.security.oauth2;

import com.velz.service.core.configuration.AppProperties;
import com.velz.service.core.configuration.helpers.CookiesHelper;
import com.velz.service.core.configuration.security.jwt.JWTHelper;
import com.velz.service.core.configuration.security.jwt.JWTType;
import com.velz.service.core.user.User;
import com.velz.service.core.user.UserService;
import com.velz.service.core.user.document.SessionToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static com.velz.service.core.configuration.security.oauth2.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.velz.service.core.user.document.SessionToken.buildSessionToken;
import static java.time.ZonedDateTime.now;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private OAuth2AuthorizationRequestRepository oauth2AuthorizationRequestRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Map<JWTType, String> tokens = JWTHelper.create(user);

        String targetUrl = determineTargetUrl(request, tokens);

        JWTHelper.addTokensToCookies(response, tokens);

        SessionToken sessionToken = buildSessionToken(tokens.get(JWTType.REFRESH));
        userService.addSessionToken(user, sessionToken);
        user.setLastSignedInAt(now());
        userService.save(user);

        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, Map<JWTType, String> tokens) {
        Optional<String> redirectUri = CookiesHelper.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid redirect URI.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(JWTType.ACCESS.getSnakeName(), tokens.get(JWTType.ACCESS))
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oauth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to.
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
