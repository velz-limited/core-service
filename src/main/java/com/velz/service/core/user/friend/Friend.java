package com.velz.service.core.user.friend;

import com.velz.service.core.configuration.audit.AuditEntity;
import com.velz.service.core.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.velz.service.core.configuration.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "friend")
@Where(clause = AE_DELETE_IS_NULL)
public class Friend extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", updatable = false)
    private User fromUser;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", updatable = false)
    private User toUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendStatus status;

    @Column(name = "last_interacted_at")
    private ZonedDateTime lastInteractedAt;

    @Column(name = "interaction_score")
    private Integer interactionScore;
}
