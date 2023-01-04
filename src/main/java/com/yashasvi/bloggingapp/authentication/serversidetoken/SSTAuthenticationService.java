package com.yashasvi.bloggingapp.authentication.serversidetoken;

import com.yashasvi.bloggingapp.authentication.AuthenticationService;
import com.yashasvi.bloggingapp.authentication.exceptions.InvalidTokenException;
import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class SSTAuthenticationService implements AuthenticationService {
    private final AuthTokenRepository authTokenRepository;

    public SSTAuthenticationService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    @Override
    public String createToken(@NonNull UserEntity userEntity) {
        var authTokenEntity = AuthTokenEntity.builder()
                .expirationTimestamp(getExpirationTimestamp())
                .user(userEntity)
                .build();
        var savedAuthTokenEntity = authTokenRepository.save(authTokenEntity);
        return String.valueOf(savedAuthTokenEntity.getToken());
    }

    @Override
    public Long getUserIdFromToken(@NonNull String token) {
        return authTokenRepository.findById(UUID.fromString(token))
                .filter(authTokenEntity -> {
                    if ((new Date()).compareTo(authTokenEntity.getExpirationTimestamp()) < 0) {
                        return true;
                    } else {
                        throw new InvalidTokenException("Authentication failure, token expired");
                    }
                })
                .map(AuthTokenEntity::getUser)
                .map(UserEntity::getId)
                .orElseThrow(() -> new InvalidTokenException("Authentication failure, invalid auth token"));
    }
}
