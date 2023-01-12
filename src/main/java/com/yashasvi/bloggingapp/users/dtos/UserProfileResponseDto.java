package com.yashasvi.bloggingapp.users.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class UserProfileResponseDto {
    @NonNull
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    private String bio;
}
