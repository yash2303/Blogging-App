package com.yashasvi.bloggingapp.users.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserRequestDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
