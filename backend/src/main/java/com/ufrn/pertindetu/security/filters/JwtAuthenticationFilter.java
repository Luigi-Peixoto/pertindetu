package com.ufrn.pertindetu.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufrn.pertindetu.base.dto.UserDetailsInfo;
import com.ufrn.pertindetu.security.utils.JwtService;
import com.ufrn.pertindetu.security.utils.ValidateTokenInCognito;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * JWT authentication filter. Coloca o email do usuário autenticado no MDC para
 * aparecer nas linhas de log da requisição; limpa o MDC no finally para não vazar entre requisições.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidateTokenInCognito validateTokenInCognito;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                final var jwt = authHeader.substring(7);
                final DecodedJWT decodedJWT = validateTokenInCognito.validateToken(jwt);
                if (Objects.nonNull(decodedJWT)) {
                    authenticateUser(request, decodedJWT);
                }
            }
            putUserEmailInMdc();
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private void putUserEmailInMdc() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsInfo user) {
            MDC.put("userEmail", user.getEmail());
        }
    }

    private void authenticateUser(HttpServletRequest request, DecodedJWT decodedJWT) {
        final List<SimpleGrantedAuthority> authorities = jwtService.getAuthorities(decodedJWT);
        final UserDetailsInfo userDetails = jwtService.getUserDetails(decodedJWT);
        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}

