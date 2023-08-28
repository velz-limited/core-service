package com.velz.service.core.user.history.region;

import com.velz.service.core.configuration.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegionHistoryRepository extends AuditJpaRepository<RegionHistory, UUID> {
}
