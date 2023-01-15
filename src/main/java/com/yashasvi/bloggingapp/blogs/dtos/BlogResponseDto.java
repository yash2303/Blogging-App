package com.yashasvi.bloggingapp.blogs.dtos;

import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BlogResponseDto {
    @NotNull
    private Long id;
    @NotEmpty
    private String title;
    @NotNull
    private UserProfileResponseDto author;
    private String summary;
    private String content;
}