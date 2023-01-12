package com.yashasvi.bloggingapp.users.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class UserResponseDto {
    @NonNull
    private Long userId;
    @NonNull
    private String username;
    @NonNull
    private String authToken;
}
