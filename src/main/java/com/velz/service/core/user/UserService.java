package com.velz.service.core.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velz.service.core.configuration.helpers.SecurityContextHelper;
import com.velz.service.core.user.document.SessionToken;
import com.velz.service.core.user.request.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    public User save(User user) {
        return userRepository.save(user);
    }

    /* Getters and Finders | Get must return an object. Find returns optional. */
    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found."));
    }

    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Email not found."));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Username not found."));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }
    /* /Getters and Finders */

    /* Granular Update */
    public void updateDisplayName(User user, String newDisplayName) {
        if (!StringUtils.equals(user.getDisplayName(), newDisplayName)) {
            user.setDisplayName(newDisplayName);
        }
    }

    public void updateUsername(User user, String newUsername) {
        if (StringUtils.isNotEmpty(newUsername) && !StringUtils.equalsIgnoreCase(user.getUsername(), newUsername)) {
            String newUsernameLowercase = newUsername.toLowerCase();
            if (userRepository.existsByUsername(newUsernameLowercase)) {
                throw new ResponseStatusException(CONFLICT, "A user already has this username.");
            }
            user.setUsername(newUsernameLowercase);
        }
    }

    public void updateEmail(User user, String newEmail, boolean emailVerified) {
        if (StringUtils.isNotEmpty(newEmail) && !StringUtils.equalsIgnoreCase(user.getEmail(), newEmail)) {
            String newEmailLowercase = newEmail.toLowerCase();
            if (userRepository.existsByEmail(newEmailLowercase)) {
                throw new ResponseStatusException(CONFLICT, "A user already has this email.");
            }
            user.setEmail(newEmailLowercase);
            user.setEmailVerified(emailVerified);
            if (!emailVerified) {
                // TODO J: Send out email verification email, do async.
            }
        }
    }

    public void updateEmail(User user, String newEmail) {
        updateEmail(user, newEmail, false);
    }

    public void updatePassword(User user, String newRawPassword) {
        if (StringUtils.isNotEmpty(newRawPassword)) {
            user.setPassword(passwordEncoder.encode(newRawPassword));
        }
    }

    public void updatePrivacy(User user, Boolean newPrivacy) {
        user.setPrivate(ofNullable(newPrivacy).orElse(false));
    }
    /* /Granular Update */

    /* Session Tokens */
    public List<SessionToken> getSessionTokens(User user) {
        JsonNode sessionTokensJsonNode = user.getSessionTokens();
        List<SessionToken> sessionTokens = new ArrayList<>();
        if (sessionTokensJsonNode != null) {
            try {
                return objectMapper.readerFor(new TypeReference<List<SessionToken>>() {}).readValue(sessionTokensJsonNode);
            } catch (IOException e) {
                user.setSessionTokens(null);
            }
        }
        return sessionTokens;
    }

    public List<SessionToken> getActiveSessionTokens(User user) {
        Date now = new Date();
        return getSessionTokens(user).stream()
                .filter(t -> !now.before(t.getIssuedAt()) && !now.after(t.getExpiresAt())).toList();
    }

    public void validateSessionToken(SessionToken sessionToken) {
        Set<ConstraintViolation<SessionToken>> violations = validator.validate(sessionToken);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public boolean existsSessionToken(User user, String hashedToken) {
        if (StringUtils.isNotEmpty(hashedToken)) {
            return getActiveSessionTokens(user).stream().anyMatch(t -> StringUtils.equals(t.getHashedToken(), hashedToken));
        }
        return false;
    }

    public void addSessionToken(User user, SessionToken sessionToken) {
        if (sessionToken != null) {
            validateSessionToken(sessionToken);
            List<SessionToken> sessionTokens = new ArrayList<>(getActiveSessionTokens(user));
            sessionTokens.add(sessionToken);
            user.setSessionTokens(objectMapper.valueToTree(sessionTokens));
        }
    }

    public void removeSessionToken(User user, String hashedToken) {
        if (StringUtils.isNotEmpty(hashedToken)) {
            List<SessionToken> sessionTokens = getActiveSessionTokens(user).stream().filter(t -> !StringUtils.equals(t.getHashedToken(), hashedToken)).toList();
            user.setSessionTokens(sessionTokens.isEmpty() ? null : objectMapper.valueToTree(sessionTokens));
        }
    }

    public void removeAllSessionTokens(User user) {
        user.setSessionTokens(null);
    }
    /* /Session Tokens */

    /* Sign Up, In, Out */
    public User signUp(UserSignUpRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID());
        SecurityContextHelper.setSecurityContext(user.getId(), user.getAuthorities());

        updateDisplayName(user, request.getDisplayName());
        updateUsername(user, request.getUsername());
        updateEmail(user, request.getEmail());
        updatePassword(user, request.getRawPassword());
        return save(user);
    }

    public User signIn(UserSignInRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(email)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Incorrect sign in details.");
        }

        User user = StringUtils.isNotEmpty(username) ? getByUsername(username) : getByEmail(email);
        SecurityContextHelper.setSecurityContext(user.getId(), user.getAuthorities());

        if (!passwordEncoder.matches(request.getRawPassword(), user.getPassword())) {
            // TODO J: Set failed sign in.
            throw new ResponseStatusException(UNAUTHORIZED, "Incorrect sign in details.");
        }
        return user;
    }

    public void signOut(User user, String hashedToken) {
        removeSessionToken(user, hashedToken);
    }

    public void signOutAll(User user) {
        removeAllSessionTokens(user);
    }
    /* /Sign Up, In, Out */

    /* Email Verification */
    public void emailVerification(UserEmailVerificationRequest request) {
        User user = getByEmail(request.getEmail());

        // Check if user is already verified.
        if (user.isEmailVerified()) {
            throw new ResponseStatusException(CONFLICT, "Email already verified.");
        }

        // TODO J: Check token is valid.

        // Verify email.
        if (StringUtils.equalsIgnoreCase(user.getEmail(), request.getEmail())) {
            user.setEmailVerified(true);
        }
    }

    public void sendEmailVerification(UserSendEmailVerificationRequest request) {
        User user = getByEmail(request.getEmail());

        // Check if user is already verified.
        if (user.isEmailVerified()) {
            throw new ResponseStatusException(CONFLICT, "Email already verified.");
        }

        // TODO J: Check when last sent.

        // TODO J: Generate token.

        // TODO J: Send email, do async.
    }
    /* /Email Verification */

    /* Password Reset */
    public void passwordReset(UserResetPasswordRequest request) {
        User user = getByEmail(request.getEmail());

        // TODO J: Check token is valid.

        // TODO J: Update password.
    }

    public void sendPasswordReset(UserSendPasswordResetRequest request) {
        User user = getByEmail(request.getEmail());

        // TODO J: Check when last sent.

        // TODO J: Generate token.

        // TODO J: Send email, do async.
    }
    /* /Password Reset */

    /* Modifications */
    public User update(User user, UserUpdateRequest request) {
        updateDisplayName(user, request.getDisplayName());
        updateUsername(user, request.getUsername());
        updateEmail(user, request.getEmail());
        updatePassword(user, request.getRawPassword());
        updatePrivacy(user, request.getIsPrivate());
        return user;
    }

    public void delete(User user) {
        user.setGoogleId(null);
        user.setFacebookId(null);
        user.setAppleId(null);
        user.setGithubId(null);
        user.setSessionTokens(null);
        userRepository.softDelete(user, user.getId());
    }
    /* /Modifications */
}
