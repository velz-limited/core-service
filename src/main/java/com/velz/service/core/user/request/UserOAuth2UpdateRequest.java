package com.velz.service.core.user.request;

import com.velz.service.core.configuration.security.oauth2.OAuth2Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOAuth2UpdateRequest {

    @NotNull
    private OAuth2Provider provider;

    @NotBlank
    private String id;

    @Email
    @NotBlank
    private String email;
}
