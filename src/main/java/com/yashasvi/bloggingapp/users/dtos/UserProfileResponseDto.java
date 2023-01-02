package com.yashasvi.bloggingapp.users.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@Builder
@EqualsAndHashCode
public class UserProfileResponseDto {
    @NonNull
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    private String bio;
}
