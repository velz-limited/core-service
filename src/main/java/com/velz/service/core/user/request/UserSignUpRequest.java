package com.velz.service.core.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.velz.service.core.user.UserService.*;

@Getter
public class UserSignUpRequest {

    @Size(min = DISPLAY_NAME_MIN, max = DISPLAY_NAME_MAX)
    @Pattern(regexp = DISPLAY_NAME_REGEX)
    @NotBlank
    private String displayName;

    @Size(min = USERNAME_MIN, max = USERNAME_MAX)
    @Pattern(regexp = USERNAME_REGEX)
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(min = RAW_PASSWORD_MIN, max = RAW_PASSWORD_MAX)
    @Pattern(regexp = RAW_PASSWORD_REGEX)
    @NotBlank
    private String rawPassword;
}
