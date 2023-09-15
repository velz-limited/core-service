package com.velz.service.core.configuration.security.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
public enum UserRole {
    GUEST(PrefixedName.GUEST, PreAuthorize.GUEST),
    STANDARD(PrefixedName.STANDARD, PreAuthorize.STANDARD),
    LOCKED(PrefixedName.LOCKED, PreAuthorize.LOCKED),
    DELETED(PrefixedName.DELETED, PreAuthorize.DELETED);

    public interface PrefixedName {
        String GUEST = "ROLE_GUEST";
        String STANDARD = "ROLE_STANDARD";
        String LOCKED = "ROLE_LOCKED";
        String DELETED = "ROLE_DELETED";
    }

    public interface PreAuthorize {
        String GUEST = "hasRole('" + PrefixedName.GUEST + "')";
        String STANDARD = "hasRole('" + PrefixedName.STANDARD + "')";
        String LOCKED = "hasRole('" + PrefixedName.LOCKED + "')";
        String DELETED = "hasRole('" + PrefixedName.DELETED + "')";
    }

    private final String prefixedName;

    private final String preAuthorize;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.getPrefixedName());
    }
}
