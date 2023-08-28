package com.velz.service.core.user.history.pinpoint;

import com.velz.service.core.configuration.audit.AuditEntity;
import com.velz.service.core.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.velz.service.core.configuration.audit.AuditEntity.AE_DELETE_IS_NULL;

@Data
@Entity
@Table(name = "pinpoint_history")
@Where(clause = AE_DELETE_IS_NULL)
public class PinpointHistory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(name = "coordinate")
    private Point<G2D> centreCoordinate;

    @Column(name = "altitude")
    private Long altitude;

    @Column(name = "visited_at")
    private ZonedDateTime visitedAt;
}
