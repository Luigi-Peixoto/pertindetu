package com.ufrn.pertindetu.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufrn.pertindetu.base.dto.UserDetailsInfo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * A service class for JWT (JSON Web Token) management in the application.
 * This class provides methods for token generation, validation, and claims extraction.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("pertindetu-api")
                .build()
                .verify(token);
    }

    public UserDetailsInfo getUserDetails(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }
        return getUserDetailsInfo(decode(token.substring(7)));
    }

    public UserDetailsInfo getUserDetails(DecodedJWT decodedJWT) {
        return getUserDetailsInfo(decodedJWT);
    }

    @NonNull
    private UserDetailsInfo getUserDetailsInfo(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        return new UserDetailsInfo(
                claims.get("id").asString(),
                claims.get("sub").asString(),
                claims.get("role").asString()
        );
    }

    public List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
        String role = decodedJWT.getClaim("role").asString();
        if (role == null) return List.of();
        return List.of(new SimpleGrantedAuthority(role));
    }
}