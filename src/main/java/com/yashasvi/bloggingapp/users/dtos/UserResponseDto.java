package com.yashasvi.bloggingapp.users.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    @NotNull
    private Long userId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String authToken;
}
