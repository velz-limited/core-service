package com.velz.service.core.user.history.region;

import com.velz.service.core.configuration.audit.AuditEntity;
import com.velz.service.core.location.region.Region;
import com.velz.service.core.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.velz.service.core.configuration.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "region_history")
@Where(clause = AE_DELETE_IS_NULL)
public class RegionHistory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "is_manual")
    private boolean manual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RegionHistoryStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
