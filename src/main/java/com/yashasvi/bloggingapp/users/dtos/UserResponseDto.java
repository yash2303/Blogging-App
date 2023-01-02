package com.yashasvi.bloggingapp.users.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@Builder
@EqualsAndHashCode
public class UserResponseDto {
    @NonNull
    private Long userId;
    @NonNull
    private String username;
    @NonNull
    private String authToken;
}
