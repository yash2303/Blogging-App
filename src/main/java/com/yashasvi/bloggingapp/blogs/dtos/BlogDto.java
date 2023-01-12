package com.yashasvi.bloggingapp.blogs.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class BlogDto {
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private Long authorId;
    private String content;
}