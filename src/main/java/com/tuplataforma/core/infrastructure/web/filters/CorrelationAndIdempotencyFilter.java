package com.tuplataforma.core.infrastructure.web.filters;

import com.tuplataforma.core.application.idempotency.IdempotencyContext;
import com.tuplataforma.core.shared.correlation.CorrelationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationAndIdempotencyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cid = request.getHeader("X-Correlation-Id");
        if (cid == null || cid.isBlank()) cid = UUID.randomUUID().toString();
        CorrelationContext.set(cid);
        String idem = request.getHeader("X-Idempotency-Key");
        if (idem != null && !idem.isBlank()) IdempotencyContext.set(idem);
        try {
            response.addHeader("X-Correlation-Id", cid);
            filterChain.doFilter(request, response);
        } finally {
            CorrelationContext.clear();
            IdempotencyContext.clear();
        }
    }
}

