package com.example.sms.infrastructure.datasource;

import lombok.Getter;

@Getter
public class ObjectOptimisticLockingFailureException extends RuntimeException {

    private final Class<?> entityType;
    private final Object identifier;

    public ObjectOptimisticLockingFailureException(Class<?> entityType, Object identifier) {
        super("Optimistic locking failed for entity type: " +
                entityType.getSimpleName() + " with identifier: " + identifier);
        this.entityType = entityType;
        this.identifier = identifier;
    }

}