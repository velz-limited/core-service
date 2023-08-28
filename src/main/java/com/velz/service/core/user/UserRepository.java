package com.velz.service.core.user;

import com.velz.service.core.configuration.audit.AuditJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends AuditJpaRepository<User, UUID> {
}
