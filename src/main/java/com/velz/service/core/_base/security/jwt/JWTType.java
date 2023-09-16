package com.velz.service.core._base.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JWTType {
    ACCESS(SnakeName.ACCESS_TOKEN),
    REFRESH(SnakeName.REFRESH_TOKEN);

    public interface SnakeName {
        String ACCESS_TOKEN = "access_token";
        String REFRESH_TOKEN = "refresh_token";
    }

    private final String snakeName;
}
