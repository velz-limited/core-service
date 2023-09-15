package com.velz.service.core.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.velz.service.core.user.UserService.*;

@Getter
public class UserResetPasswordRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min = RAW_PASSWORD_MIN, max = RAW_PASSWORD_MAX)
    @NotBlank
    private String rawPassword;

    @Size(min = SEND_TOKEN_MIN, max = SEND_TOKEN_MAX)
    @NotBlank
    private String token;
}
