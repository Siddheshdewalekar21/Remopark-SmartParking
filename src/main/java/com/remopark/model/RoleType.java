package com.remopark.model;

public enum RoleType {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MODERATOR,
    ROLE_STAFF;

    public String getName() {
        return this.name();
    }
} 