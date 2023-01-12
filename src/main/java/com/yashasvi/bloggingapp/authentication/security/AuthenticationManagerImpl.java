package com.yashasvi.bloggingapp.authentication.security;

import com.yashasvi.bloggingapp.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManagerImpl implements AuthenticationManager {
    private final AuthenticationService authenticationService;

    public AuthenticationManagerImpl(@Qualifier("JwtAuthenticationService") AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AuthenticationImpl authenticationImpl) {
            var token = authenticationImpl.getCredentials();
            var userId = authenticationService.getUserIdFromToken(token);
            authenticationImpl.setUserId(userId);
            return authenticationImpl;
        }
        return null;
    }
}
