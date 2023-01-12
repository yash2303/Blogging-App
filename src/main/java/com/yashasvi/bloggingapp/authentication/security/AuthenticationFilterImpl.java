package com.yashasvi.bloggingapp.authentication.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterImpl extends AuthenticationFilter {
    public AuthenticationFilterImpl(AuthenticationManager authenticationManager, AuthenticationConverter authenticationConverter) {
        super(authenticationManager, authenticationConverter);
        this.setSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }
}
