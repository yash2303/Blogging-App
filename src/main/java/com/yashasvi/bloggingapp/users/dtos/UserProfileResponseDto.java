package com.yashasvi.bloggingapp.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseDto {
    @NotNull
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")
    private String email;
    private String bio;
}
