package com.velz.service.core.user.history.attraction;

import com.velz.service.core._base.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttractionHistoryRepository extends AuditJpaRepository<AttractionHistory, UUID> {
}
