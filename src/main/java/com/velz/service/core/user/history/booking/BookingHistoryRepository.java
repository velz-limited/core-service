package com.velz.service.core.user.history.booking;

import com.velz.service.core._base.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingHistoryRepository extends AuditJpaRepository<BookingHistory, UUID> {
}
