package com.ufrn.pertindetu.security.filters;

import com.ufrn.pertindetu.base.dto.UserDetailsInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that logs API requests for usage monitoring (controllers, modules, pages).
 * Logs method, path, query string, authenticated user, response status and duration.
 * Only applies to paths under {@code /v1/} to focus on application API usage.
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String API_PATH_PREFIX = "/v1/";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (!path.contains(API_PATH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startNanos = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = (System.nanoTime() - startNanos) / 1_000_000;
            logRequest(request, response, durationMs);
        }
    }

    private void logRequest(HttpServletRequest request, HttpServletResponse response, long durationMs) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String user = resolveUser();
        int status = response.getStatus();

        StringBuilder msg = new StringBuilder()
                .append("[USER_REQUEST] method=").append(method)
                .append(" path=").append(path);
        if (query != null && !query.isBlank()) {
            msg.append(" query=").append(sanitizeQuery(query));
        }
        msg.append(" user=").append(user)
                .append(" status=").append(status)
                .append(" durationMs=").append(durationMs);

        LOG.debug("{}", msg);
    }

    private String resolveUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsInfo user) {
            return user.getEmail() != null ? user.getEmail() : "unknown";
        }
        return "anonymous";
    }

    /**
     * Removes known sensitive query parameter values from the logged string.
     *
     * @param query the raw query string (e.g. from request.getQueryString())
     * @return the query string with sensitive values masked, or empty if null/blank
     */
    private String sanitizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return "";
        }
        return query.replaceAll("(?i)(token|password|key|secret)=[^&]*", "$1=***");
    }
}

