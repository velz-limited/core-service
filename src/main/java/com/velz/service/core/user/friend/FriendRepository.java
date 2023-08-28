package com.velz.service.core.user.friend;

import com.velz.service.core.configuration.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendRepository extends AuditJpaRepository<Friend, UUID> {
}
