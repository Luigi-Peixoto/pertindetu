package com.ufrn.pertindetu.security.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.SigningKeyNotFoundException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufrn.pertindetu.base.utils.exception.JWTException;
import com.ufrn.pertindetu.base.utils.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

/**
 * Service for validating JWT tokens issued by AWS Cognito.
 * <p>
 * This class decodes and verifies JWT tokens using Cognito's JWKS (JSON Web Key Set)
 * endpoint. Tokens are validated against the configured client ID (audience) and
 * the corresponding public key retrieved from Cognito.
 * <p>
 * It uses caching and rate limiting for JWKS key retrieval to improve performance.
 */
@Service
public class ValidateTokenInCognito {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateTokenInCognito.class);

    private final JwkProvider jwkProvider;
    private final String clientId;

    /**
     * Constructs the service and initializes the JWK provider.
     *
     * @param region     the AWS region of the Cognito user pool
     * @param userPoolId the Cognito user pool ID
     * @param clientId   the Cognito app client ID (used as JWT audience)
     * @throws JWTException         if there is an error initializing the JWK provider
     * @throws MalformedURLException if the JWKS URL is invalid
     */
    public ValidateTokenInCognito(@Value("${sso.region}") String region,
                                  @Value("${sso.cognitoUserPoolId}") String userPoolId,
                                  @Value("${sso.cognitoClientId}") String clientId) throws JWTException, MalformedURLException {
        this.clientId = clientId;

        String jwksUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", region, userPoolId);

        this.jwkProvider = new JwkProviderBuilder(new URL(jwksUrl))
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(30, 1, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Validates a JWT token against Cognito's public keys and the configured client ID.
     *
     * @param token the JWT token to validate
     * @return the decoded and verified JWT, or null if verification fails
     */
    public DecodedJWT validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);

            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withAudience(clientId)
                    .build();

            DecodedJWT verifiedJwt = verifier.verify(token);

            LogUtils.debug(LOGGER, "Token verified successfully");

            return verifiedJwt;
        } catch (TokenExpiredException e) {
            LogUtils.warn(LOGGER, "Token expired: {}", e.getMessage());
        } catch (SigningKeyNotFoundException | JWTVerificationException e) {
            LogUtils.error(LOGGER, "JWT verification failed [{}]: {}", e.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            LogUtils.error(LOGGER, e.getMessage(), e);
            throw new JWTException("Error validating token", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}

