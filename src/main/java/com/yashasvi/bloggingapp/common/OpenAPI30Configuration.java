package com.yashasvi.bloggingapp.common;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "A JWT token is required to access this API. JWT token can be obtained by User Register or Login APIs"
)
public class OpenAPI30Configuration {
}