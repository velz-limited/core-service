package com.velz.service.core.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSendPasswordResetRequest {

    @Email
    @NotBlank
    private String email;
}
