package com.velz.service.core.configuration.security.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
public enum UserRole {
    GUEST(PrefixedName.GUEST),
    STANDARD(PrefixedName.STANDARD),
    LOCKED(PrefixedName.LOCKED),
    DELETED(PrefixedName.DELETED);

    private interface PrefixedName {
        String GUEST = "ROLE_GUEST";
        String STANDARD = "ROLE_STANDARD";
        String LOCKED = "ROLE_LOCKED";
        String DELETED = "ROLE_DELETED";
    }

    private final String prefixedName;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.getPrefixedName());
    }
}
