package com.yashasvi.bloggingapp.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    private String bio;
}
