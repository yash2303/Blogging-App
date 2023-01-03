package com.yashasvi.bloggingapp.authentication;

import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.lang.NonNull;

public interface AuthenticationService {
    String createToken(@NonNull UserEntity userEntity);

    Long getUserIdFromToken(@NonNull String token);
}
