package com.yashasvi.bloggingapp.blogs.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class UpdateBlogRequestDto {
    private String title;
    private String content;
}
