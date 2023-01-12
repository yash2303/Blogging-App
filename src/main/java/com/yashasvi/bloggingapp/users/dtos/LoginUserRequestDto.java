package com.yashasvi.bloggingapp.users.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class LoginUserRequestDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
