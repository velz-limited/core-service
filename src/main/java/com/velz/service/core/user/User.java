package com.velz.service.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.velz.service.core._base.audit.AuditEntity;
import com.velz.service.core._base.generators.annotations.RandomUuidGenerator;
import com.velz.service.core._base.security.jwt.JWTUserClaims;
import com.velz.service.core._base.security.principal.UserRole;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.velz.service.core._base.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "users")
@Where(clause = AE_DELETE_IS_NULL)
public class User extends AuditEntity implements JWTUserClaims, OAuth2User {

    @Id
    @RandomUuidGenerator
    private UUID id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "email_verifications")
    private JsonNode emailVerifications;

    @Column(name = "is_email_verified")
    private boolean isEmailVerified;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "password_resets")
    private JsonNode passwordResets;

    @JsonIgnore
    @Column(name = "google_id")
    private String googleId;

    @JsonIgnore
    @Column(name = "facebook_id")
    private String facebookId;

    @JsonIgnore
    @Column(name = "apple_id")
    private String appleId;

    @JsonIgnore
    @Column(name = "github_id")
    private String githubId;

    @Column(name = "last_signed_in_at")
    private ZonedDateTime lastSignedInAt;

    @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "session_tokens")
    private JsonNode sessionTokens;

    @JsonIgnore
    @Type(JsonType.class)
    @Column(name = "failed_sign_ins")
    private JsonNode failedSignIns;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "profile_description")
    private String profileDescription;

    @Column(name = "profile_image_uri")
    private URI profileImageUri;

    @Type(JsonType.class)
    @Column(name = "social_links")
    private JsonNode socialLinks;

    public UserRole getRole() {
        if (getId() == null) {
            return UserRole.GUEST;
        }

        if (getDeletedAt() != null) {
            return UserRole.DELETED;
        }

        if (isLocked()) {
            return UserRole.LOCKED;
        }

        return UserRole.STANDARD;
    }

    /* JWTUserClaims */
    @JsonIgnore
    @Override
    public String getIdString() {
        return getId().toString();
    }

    @JsonIgnore
    @Override
    public String getRoleString() {
        return getRole().name();
    }
    /* /JWTUserClaims */

    /* OAuth2User */
    @JsonIgnore
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(getRole().getAuthority());
    }

    @JsonIgnore
    @Override
    public String getName() {
        return getDisplayName();
    }
    /* /OAuth2User */
}
