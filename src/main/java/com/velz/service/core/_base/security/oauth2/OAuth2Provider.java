package com.velz.service.core._base.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2Provider {
    GOOGLE(Name.GOOGLE),
    FACEBOOK(Name.FACEBOOK),
    GITHUB(Name.GITHUB);

    public interface Name {
        String GOOGLE = "google";
        String FACEBOOK = "facebook";
        String GITHUB = "github";
    }

    private final String name;
}
