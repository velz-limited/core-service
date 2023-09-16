package com.velz.service.core.user.request;

import com.velz.service.core._base.security.oauth2.OAuth2Provider;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import static com.velz.service.core.user.UserService.*;

@Getter
@Setter
public class UserOAuth2SignUpRequest {

    @NotNull
    private OAuth2Provider provider;

    @NotBlank
    private String id;

    @Email
    @NotBlank
    private String email;

    @Size(min = DISPLAY_NAME_MIN, max = DISPLAY_NAME_MAX)
    @Pattern(regexp = DISPLAY_NAME_REGEX)
    @NotBlank
    private String displayName;

    @Size(min = USERNAME_MIN, max = USERNAME_MAX)
    @Pattern(regexp = USERNAME_REGEX)
    @NotBlank
    private String username;
}
