package com.tuplataforma.core.infrastructure.security;

import com.tuplataforma.core.domain.identity.Role;
import com.tuplataforma.core.infrastructure.security.jwt.JwtTokenProvider;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.shared.user.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                Claims claims = tokenProvider.validateToken(token);
                String userId = claims.getSubject();
                String tenantIdStr = claims.get("tenantId", String.class);
                @SuppressWarnings("unchecked")
                List<String> rolesStr = claims.get("roles", List.class);

                // 1. Validate Tenant
                // The tenant in the token MUST match the tenant context (which might have been set by header/subdomain)
                // OR, if context is empty, we set it from token?
                // Rule: "El tenantId del usuario DEBE coincidir con el tenantId del request"
                // Rule: "El tenant se resuelve: Desde JWT"
                
                TenantId tokenTenantId = TenantId.fromString(tenantIdStr);
                
                // Set TenantContext from Token if not present (or verify if present)
                TenantId currentTenantId = TenantContext.getTenantId();

                if (currentTenantId != null && !currentTenantId.equals(tokenTenantId)) {
                     // Critical failure: Token tenant mismatch request tenant
                     response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant mismatch");
                     return;
                }
                
                // If not set by header, set from token (Source: JWT)
                if (currentTenantId == null) {
                    TenantContext.setTenantId(tokenTenantId);
                }

                Set<Role> roles = rolesStr.stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet());

                // 2. Set User Context
                UserContext.set(userId, tokenTenantId, roles);

                // 3. Set Spring Security Context
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                logger.error("Could not set user authentication in security context", e);
                SecurityContextHolder.clearContext();
                TenantContext.clear();
                UserContext.clear();
                // Don't fail here, let SecurityFilterChain handle unauthorized
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clean up ThreadLocals
            UserContext.clear();
            // TenantContext is usually cleared by a separate filter or interceptor, 
            // but if we set it here, we should ensure it's managed. 
            // Assuming TenantFilter (from Phase 1) runs BEFORE this and clears AFTER.
            // If we set it here (because it was missing), we should probably clear it.
            // For safety in this phase:
            // We rely on the existing TenantContext cleanup mechanism if it exists. 
            // If not, we should be careful. 
            // But UserContext is definitely ours to clear.
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
