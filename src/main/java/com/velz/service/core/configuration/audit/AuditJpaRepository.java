package com.velz.service.core.configuration.audit;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

import static java.time.ZonedDateTime.now;

@NoRepositoryBean
public interface AuditJpaRepository<T extends AuditEntity, I> extends JpaRepository<T, I> {

    default void softDelete(@NonNull T entity) {
        entity.setDeletedAt(now());
        // TODO J: Get actual user.
        entity.setDeletedById(UUID.randomUUID());
        save(entity);
    }

    default void softDeleteById(@NonNull I entityId) {
        softDelete(findById(entityId).orElseThrow());
    }
}
