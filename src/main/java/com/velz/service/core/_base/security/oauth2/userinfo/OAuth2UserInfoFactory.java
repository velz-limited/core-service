package com.velz.service.core._base.security.oauth2.userinfo;

import org.springframework.security.authentication.ProviderNotFoundException;

import java.util.Map;

import static com.velz.service.core._base.security.oauth2.OAuth2Provider.*;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (GOOGLE.getName().equalsIgnoreCase(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (FACEBOOK.getName().equalsIgnoreCase(registrationId)) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (GITHUB.getName().equalsIgnoreCase(registrationId)) {
            return new GitHubOAuth2UserInfo(attributes);
        } else {
            throw new ProviderNotFoundException("Provider " + registrationId + " is not supported yet.");
        }
    }
}
