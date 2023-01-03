package com.yashasvi.bloggingapp.authentication.jsonwebtoken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yashasvi.bloggingapp.authentication.AuthenticationService;
import com.yashasvi.bloggingapp.authentication.exceptions.InvalidTokenException;
import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class JWTAuthenticationService implements AuthenticationService {
    private final Algorithm algorithm;

    public JWTAuthenticationService(@Value("${auth.jwt-secret}") String jwtSecret) {
        algorithm = Algorithm.HMAC256(jwtSecret);
    }

    @Override
    public String createToken(@NonNull UserEntity userEntity) {
        if (Objects.isNull(userEntity.getId())) {
            throw new IllegalArgumentException("User id is null");
        }
        return JWT.create()
                .withSubject(String.valueOf(userEntity.getId()))
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    @Override
    public Long getUserIdFromToken(@NonNull String token) {
        try {
            return Long.valueOf(JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject());
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Authentication failure, invalid JWT token");
        }
    }
}
