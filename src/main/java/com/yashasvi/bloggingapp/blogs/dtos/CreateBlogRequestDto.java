package com.yashasvi.bloggingapp.blogs.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreateBlogRequestDto {
    @NotEmpty
    private String title;
    @NonNull
    private String content;
}
