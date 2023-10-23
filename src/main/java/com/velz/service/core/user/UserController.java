package com.velz.service.core.user;

import com.velz.service.core._base.security.jwt.JWTHelper;
import com.velz.service.core._base.security.jwt.JWTType;
import com.velz.service.core.user.request.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.velz.service.core._base.security.jwt.JWTHelper.*;
import static com.velz.service.core.user.document.SessionToken.buildSessionToken;
import static com.velz.service.core.user.document.SessionToken.hashToken;
import static java.time.ZonedDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    /* Auth */
    @Transactional
    @ResponseStatus(CREATED)
    @PostMapping("/auth/sign-up")
    public Map<String, Object> signUp(@Valid @RequestBody UserSignUpRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        User user = userService.signUp(request);
        Map<JWTType, String> tokens = JWTHelper.create(user);
        userService.addSessionToken(user, buildSessionToken(tokens.get(JWTType.REFRESH)));
        user.setLastSignedInAt(now());
        return respondWithUserAndTokens(servletRequest, servletResponse, user, tokens);
    }

    @Transactional
    @PostMapping("/auth/sign-in")
    public Map<String, Object> signIn(@Valid @RequestBody UserSignInRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        User user = userService.signIn(request);
        Map<JWTType, String> tokens = JWTHelper.create(user);
        userService.addSessionToken(user, buildSessionToken(tokens.get(JWTType.REFRESH)));
        user.setLastSignedInAt(now());
        return respondWithUserAndTokens(servletRequest, servletResponse, user, tokens);
    }

    @GetMapping("/auth/refresh-token")
    public Map<String, Object> refreshToken(
            @CookieValue(name = JWTType.SnakeName.REFRESH_TOKEN, required = false) String refreshToken,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        Jws<Claims> claims = parseClaims(refreshToken);
        if (!validateClaims(JWTType.REFRESH, claims)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid refresh token.");
        }

        User user = userService.getById(UUID.fromString(claims.getBody().getSubject()));
        if (!userService.existsSessionToken(user, hashToken(refreshToken))) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid refresh token.");
        }

        Map<JWTType, String> tokens = JWTHelper.refresh(user);
        return respondWithUserAndTokens(servletRequest, servletResponse, user, tokens);
    }

    private static Map<String, Object> respondWithUserAndTokens(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse, User user, Map<JWTType, String> tokens) {

        Map<String, Object> map = new HashMap<>();
        map.put("user_details", user);
        addTokensToCookies(servletRequest, servletResponse, tokens);
        addTokensToMapBody(map, tokens);
        return map;
    }

    @Transactional
    @GetMapping("/auth/sign-out")
    public void signOut(
            @AuthenticationPrincipal UUID id,
            @CookieValue(name = JWTType.SnakeName.REFRESH_TOKEN, required = false) String refreshToken,
            HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        User user = userService.getById(id);
        userService.signOut(user, hashToken(refreshToken));
        deleteTokensFromCookies(servletRequest, servletResponse);
    }

    @Transactional
    @GetMapping("/auth/sign-out-all")
    public void signOutAll(@AuthenticationPrincipal UUID id, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        userService.signOutAll(userService.getById(id));
        deleteTokensFromCookies(servletRequest, servletResponse);
    }

    @Transactional
    @PutMapping("/auth/email-verification")
    public void emailVerification(@Valid @RequestBody UserEmailVerificationRequest request) {
        userService.emailVerification(request);
    }

    @Transactional
    @PostMapping("/auth/send-email-verification")
    public void sendEmailVerification(@Valid @RequestBody UserSendEmailVerificationRequest request) {
        userService.sendEmailVerification(request);
    }

    @Transactional
    @PutMapping("/auth/password-reset")
    public void passwordReset(@Valid @RequestBody UserResetPasswordRequest request) {
        userService.passwordReset(request);
    }

    @Transactional
    @PostMapping("/auth/send-password-reset")
    public void sendPasswordReset(@Valid @RequestBody UserSendPasswordResetRequest request) {
        userService.sendPasswordReset(request);
    }

    @GetMapping("/auth/details")
    public User get(@AuthenticationPrincipal UUID id) {
        return userService.getById(id);
    }

    @Transactional
    @PutMapping("/auth/update")
    public User update(@AuthenticationPrincipal UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return userService.update(userService.getById(id), request);
    }

    @Transactional
    @DeleteMapping("/auth/delete")
    public void delete(@AuthenticationPrincipal UUID id) {
        userService.delete(userService.getById(id));
    }
    /* /Auth */
}
