package com.velz.service.core.configuration.security.oauth2.userinfo;

import com.velz.service.core.configuration.security.oauth2.OAuth2Provider;

import java.util.Map;

public class GitHubOAuth2UserInfo extends OAuth2UserInfo {

    public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GITHUB;
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
