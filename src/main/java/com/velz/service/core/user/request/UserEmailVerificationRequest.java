package com.velz.service.core.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.velz.service.core.user.UserService.SEND_TOKEN_MAX;
import static com.velz.service.core.user.UserService.SEND_TOKEN_MIN;

@Getter
public class UserEmailVerificationRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min = SEND_TOKEN_MIN, max = SEND_TOKEN_MAX)
    @NotBlank
    private String token;
}
