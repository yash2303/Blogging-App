package com.yashasvi.bloggingapp.authentication.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationConverterImpl implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.substring(7);
            return new AuthenticationImpl(token);
        }
        return null;
    }
}
