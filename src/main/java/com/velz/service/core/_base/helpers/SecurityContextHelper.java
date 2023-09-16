package com.velz.service.core._base.helpers;

import com.velz.service.core.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class SecurityContextHelper {

    private SecurityContextHelper() {
        throw new UnsupportedOperationException();
    }

    public static void setSecurityContext(UUID principal, Collection<? extends GrantedAuthority> grantedAuthorities) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorities));
        SecurityContextHolder.setContext(context);
    }

    public static void setSecurityContext(UUID principal, GrantedAuthority grantedAuthority) {
        setSecurityContext(principal, Collections.singletonList(grantedAuthority));
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    public static UUID getAuthenticatedPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        if (!authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) { // TODO J: Avoid referencing non-helper related objects.
            return user.getId();
        }

        return (UUID) principal;
    }
}
