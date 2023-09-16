package com.velz.service.core._base.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity {

    public static final String AE_DELETE_IS_NULL = "deleted_at IS NULL";

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private UUID createdById;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private ZonedDateTime lastModifiedAt;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private UUID lastModifiedById;

    @JsonIgnore
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @JsonIgnore
    @Column(name = "deleted_by")
    private UUID deletedById;
}
