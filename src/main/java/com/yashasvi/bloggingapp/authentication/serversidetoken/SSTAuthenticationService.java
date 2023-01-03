package com.yashasvi.bloggingapp.authentication.serversidetoken;

import com.yashasvi.bloggingapp.authentication.AuthenticationService;
import com.yashasvi.bloggingapp.users.UserEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class SSTAuthenticationService implements AuthenticationService {
    @Override
    public String createToken(@NonNull UserEntity userEntity) {
        return null;
    }

    @Override
    public Long getUserIdFromToken(@NonNull String token) {
        return null;
    }
}
