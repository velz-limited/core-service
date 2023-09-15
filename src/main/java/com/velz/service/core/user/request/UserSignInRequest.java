package com.velz.service.core.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignInRequest {

    // Don't require heavy validation for sign in details since the rules can change in the future.

    @Size(max = 255)
    private String username;

    @Email
    private String email;

    @Size(max = 255)
    @NotBlank
    private String rawPassword;
}
