package com.roc.app.user.general;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    PARTICIPANT,
    REFEREE,
    ORGANIZER;

    @Override
    public String getAuthority() {
        return name();
    }
}
