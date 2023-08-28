package com.velz.service.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.velz.service.core.configuration.audit.AuditEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.velz.service.core.configuration.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "users")
@Where(clause = AE_DELETE_IS_NULL)
public class User extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "is_verified")
    private boolean verified;

    @Column(name = "verification_attempted_at")
    private ZonedDateTime verificationAttemptedAt;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "is_private")
    private boolean privateUser; // The word 'private' is reserved, using 'privateUser' instead.

    @Column(name = "last_signed_in_at")
    private ZonedDateTime lastSignedInAt;

    @Column(name = "profile_image_uri")
    private URI profileImageUri;

    @Type(JsonType.class)
    @Column(name = "social_links")
    private JsonNode socialLinks;
}
