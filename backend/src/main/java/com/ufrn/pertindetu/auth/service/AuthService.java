package com.ufrn.pertindetu.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ufrn.pertindetu.auth.dto.LoginDTO;
import com.ufrn.pertindetu.auth.dto.TokenDTO;
import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret:minha_chave_super_secreta_123}")
    private String secret;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public TokenDTO login(LoginDTO loginDTO) {
        // 1. Busca o usuário pelo email
        User user = userRepository.findByEmailAndActiveTrue(loginDTO.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas.", HttpStatus.UNAUTHORIZED));

        // 2. Verifica se a senha está correta
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("Credenciais inválidas.", HttpStatus.UNAUTHORIZED);
        }

        // 3. Gera o Token JWT com validade de 2 horas
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer("pertindetu-api")
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);

        return new TokenDTO(token);
    }
}