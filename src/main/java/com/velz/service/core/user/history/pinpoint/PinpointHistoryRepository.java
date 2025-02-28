package com.velz.service.core.user.history.pinpoint;

import com.velz.service.core._base.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PinpointHistoryRepository extends AuditJpaRepository<PinpointHistory, UUID> {
}
