package com.yashasvi.bloggingapp.authentication;

import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.lang.NonNull;

import java.util.Date;

public interface AuthenticationService {
    Long TOKEN_EXPIRY_PERIOD = 7 * 86400 * 1000L; // 7 days

    String createToken(@NonNull UserEntity userEntity);

    Long getUserIdFromToken(@NonNull String token);

    default Date getExpirationTimestamp() {
        var currentTimestamp = (new Date()).toInstant().toEpochMilli();
        var expirationTimestamp = currentTimestamp + TOKEN_EXPIRY_PERIOD;
        return new Date(expirationTimestamp);
    }
}
