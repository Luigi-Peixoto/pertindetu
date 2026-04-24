package com.ufrn.pertindetu.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

    @Value("${sso.permission.application.name}")
    private String applicationName;

    @Value("${sso.permission.application.module.name}")
    private String moduleName;

    public DecodedJWT decode(String token) {
        return JWT.decode(token);
    }

    public UserDetailsInfo getUserDetails(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }
        DecodedJWT decodedJWT = decode(token.substring(7));
        return getUserDetailsInfo(decodedJWT);
    }

    public UserDetailsInfo getUserDetails(DecodedJWT decodedJWT) {
        return getUserDetailsInfo(decodedJWT);
    }

    @NonNull
    private UserDetailsInfo getUserDetailsInfo(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();

        String id = claims.get("sub").asString();
        String username = claims.get("name").asString();
        String email = claims.get("email").asString();

        return new UserDetailsInfo(id, username, email);
    }

    public List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
        String accessData = CompressionUtils.decodeAndDecompress(decodedJWT.getClaims().get("access_data").asString());

        JsonElement jsonElement = JsonParser.parseString(accessData);
        List<String> permissions = jsonElement
                .getAsJsonObject()
                .getAsJsonObject("roles")
                .getAsJsonObject(applicationName)
                .getAsJsonArray(moduleName)
                .asList().stream()
                .map(JsonElement::getAsString)
                .toList();

        return permissions.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
