package com.yashasvi.bloggingapp.blogs.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class FeedDto {
    @NonNull
    private List<BlogDto> blogListDto;
}
