package com.velz.service.core._base.audit;

import com.velz.service.core._base.helpers.SecurityContextHelper;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

import static java.time.ZonedDateTime.now;

@NoRepositoryBean
public interface AuditJpaRepository<T extends AuditEntity, I> extends JpaRepository<T, I> {

    default void softDelete(@NonNull T entity) {
        entity.setDeletedAt(now());
        entity.setDeletedById(SecurityContextHelper.getAuthenticatedPrincipal());
    }

    default void softDelete(@NonNull T entity, I deletedById) {
        entity.setDeletedAt(now());
        entity.setDeletedById((UUID) deletedById);
    }

    // TODO J: Don't load entity when deleting by id.
    default void softDeleteById(@NonNull I entityId, I deletedById) {
        softDelete(findById(entityId).orElseThrow(), deletedById);
    }
}
