package com.velz.service.core.configuration.security.jwt;

import com.velz.service.core.configuration.security.principal.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.velz.service.core.configuration.helpers.RequestHelper.getBearerToken;
import static com.velz.service.core.configuration.helpers.SecurityContextHelper.clearSecurityContext;
import static com.velz.service.core.configuration.helpers.SecurityContextHelper.setSecurityContext;
import static com.velz.service.core.configuration.security.jwt.JWTHelper.*;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Jws<Claims> claims = parseClaims(getBearerToken(request));
            if (validateClaims(JWTType.ACCESS, claims)) {
                UUID id = UUID.fromString(claims.getBody().getSubject());
                UserRole role = UserRole.valueOf(claims.getBody().get(ROLE_CLAIM, String.class).toUpperCase());
                setSecurityContext(id, role.getAuthority());
            } else {
                clearSecurityContext();
            }
        } catch (Exception e) {
            log.error("An exception occurred during JWT authentication.", e);
        }

        filterChain.doFilter(request, response);
    }
}
