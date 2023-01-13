package com.yashasvi.bloggingapp.blogs.dtos;

import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BlogResponseDto {
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private UserProfileResponseDto author;
    private String content;
}