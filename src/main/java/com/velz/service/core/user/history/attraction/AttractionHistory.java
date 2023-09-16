package com.velz.service.core.user.history.attraction;

import com.velz.service.core._base.audit.AuditEntity;
import com.velz.service.core.attraction.Attraction;
import com.velz.service.core.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.velz.service.core._base.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "attraction_history")
@Where(clause = AE_DELETE_IS_NULL)
public class AttractionHistory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    @Column(name = "is_manual")
    private boolean isManual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttractionHistoryStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
