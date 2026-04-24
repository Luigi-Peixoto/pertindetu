package com.ufrn.pertindetu.security.utils;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


/**
 * Custom implementation of Spring Security's PermissionEvaluator.
 * <p>
 * Evaluates whether the authenticated user has a specific permission for a given module.
 * Permissions are expected to be prefixed with the module name followed by a colon.
 * This evaluator only supports the domain object-based permission check.
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    /**
     * Checks if the authenticated user has the required permission for the given target domain object.
     *
     * @param authentication     the current Authentication object containing user authorities
     * @param targetDomainObject the target module or domain object (used as a prefix for permission check)
     * @param permission         the required permission to check
     * @return true if the user has the required permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString();
        String module = targetDomainObject.toString().concat(":");

        List<? extends GrantedAuthority> filteredList = authentication.getAuthorities()
                .stream()
                .filter(p -> p.getAuthority().startsWith(module))
                .toList();

        if (filteredList.isEmpty()) {
            return false;
        }

        return filteredList.stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals(module + requiredPermission));
    }

    /**
     * Not supported in this implementation.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

