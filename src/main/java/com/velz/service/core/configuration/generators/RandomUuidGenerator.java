package com.velz.service.core.configuration.generators;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.descriptor.java.UUIDJavaType;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.EnumSet;
import java.util.UUID;

public class RandomUuidGenerator implements BeforeExecutionGenerator {

    private final transient UUIDJavaType.ValueTransformer valueTransformer;

    public RandomUuidGenerator(Member idMember) {
        Class<?> propertyType = ReflectHelper.getPropertyType(idMember);
        if (UUID.class.isAssignableFrom(propertyType)) {
            this.valueTransformer = UUIDJavaType.PassThroughTransformer.INSTANCE;
        } else if (String.class.isAssignableFrom(propertyType)) {
            this.valueTransformer = UUIDJavaType.ToStringTransformer.INSTANCE;
        } else {
            if (!byte[].class.isAssignableFrom(propertyType)) {
                throw new HibernateException("Unanticipated return type [" + propertyType.getName() + "] for UUID conversion");
            }

            this.valueTransformer = UUIDJavaType.ToBytesTransformer.INSTANCE;
        }
    }

    public RandomUuidGenerator(com.velz.service.core.configuration.generators.annotations.RandomUuidGenerator config, Member idMember, CustomIdGeneratorCreationContext creationContext) {
        this(idMember);
    }

    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }

    public Serializable generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        // If a UUID has been set, then use that instead of generating new.
        UUID uuid = (UUID) session.getEntityPersister(null, owner).getIdentifier(owner, session);
        return this.valueTransformer.transform(uuid != null ? uuid : StandardRandomStrategy.INSTANCE.generateUuid(session));
    }
}
