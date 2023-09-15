package com.velz.service.core.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static final int DISPLAY_NAME_MIN = 1;
    public static final int DISPLAY_NAME_MAX = 48;
    public static final String DISPLAY_NAME_REGEX = "^(?![ ,.'-])(?!.*([ ,.'-])\\1)[\\p{L} ,.'-]*$";

    public static final int USERNAME_MIN = 3;
    public static final int USERNAME_MAX = 24;
    public static final String USERNAME_REGEX = "^(?![-._])(?!.*[-._]{2})[a-zA-Z0-9-._]+(?<![-._])$";

    public static final int RAW_PASSWORD_MIN = 8;
    public static final int RAW_PASSWORD_MAX = 256;
    public static final String RAW_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[`¬!\"£$%^&*()_=+{};:'@#~<>,.?|\\-\\[\\]\\/\\\\]).{0,}$";

    // TODO J: Set real min/max for this.
    public static final int SEND_TOKEN_MIN = 0;
    public static final int SEND_TOKEN_MAX = 50;
}
