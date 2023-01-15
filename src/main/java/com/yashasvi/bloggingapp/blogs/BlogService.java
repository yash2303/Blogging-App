package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.blogs.dtos.BlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.blogs.dtos.UpdateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.exceptions.BlogNotFoundException;
import com.yashasvi.bloggingapp.blogs.exceptions.UserNotAuthorisedException;
import com.yashasvi.bloggingapp.blogs.summarizer.TextSummarizer;
import com.yashasvi.bloggingapp.users.UserEntity;
import com.yashasvi.bloggingapp.users.UserRepository;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlogService {
    private static final int NUMBER_OF_SENTENCES_IN_SUMMARY = 4;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final TextSummarizer textSummarizer;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository, TextSummarizer textSummarizer) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.textSummarizer = textSummarizer;
    }

    public BlogResponseDto createBlog(Long authorId, CreateBlogRequestDto createBlogRequestDto) {
        var userEntity = userRepository.findById(authorId);
        var blogSummary = textSummarizer.summarize(createBlogRequestDto.getContent(), NUMBER_OF_SENTENCES_IN_SUMMARY);
        return userEntity.map(author -> BlogEntity.builder()
                        .author(author)
                        .title(createBlogRequestDto.getTitle())
                        .content(createBlogRequestDto.getContent())
                        .summary(blogSummary)
                        .build())
                .map(blogRepository::save)
                .map(this::convertToBlogResponseDto)
                .orElseThrow(() -> new UserNotFoundException("Author not found"));
    }

    public BlogResponseDto updateBlog(Long userId, Long blogId, UpdateBlogRequestDto updateBlogRequestDto) {
        var blogEntity = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with input blogId doesn't exist"));
        if (!userId.equals(blogEntity.getAuthor().getId())) {
            throw new UserNotAuthorisedException("Only author is authorised to update the blog");
        }
        if (Objects.nonNull(updateBlogRequestDto.getTitle())) {
            blogEntity.setTitle(updateBlogRequestDto.getTitle());
        }
        if (Objects.nonNull(updateBlogRequestDto.getContent())) {
            var blogSummary = textSummarizer.summarize(updateBlogRequestDto.getContent(), NUMBER_OF_SENTENCES_IN_SUMMARY);
            blogEntity.setSummary(blogSummary);
            blogEntity.setContent(updateBlogRequestDto.getContent());
        }
        var savedBlogEntity = blogRepository.save(blogEntity);
        return convertToBlogResponseDto(savedBlogEntity);
    }

    public BlogResponseDto getBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .filter(blogEntity -> !blogEntity.isDeleted())
                .map(this::convertToBlogResponseDto)
                .orElseThrow(() -> new BlogNotFoundException("Blog with input blogId doesn't exist"));
    }

    public void deleteBlog(Long blogId) {
        var blogEntity = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with input blogId doesn't exist"));
        blogEntity.setDeleted(true);
        blogRepository.save(blogEntity);
    }

    // TODO: Add sorting and pagination logic in all FeedDto
    // TODO: Remove blog content from FeedDto, will help reduce payload size
    public FeedDto getGeneralFeed() {
        var blogEntities = blogRepository.findAll();
        return convertToFeedDto(blogEntities);
    }

    public FeedDto getBlogsByAuthor(Long authorId) {
        var author = userRepository.findById(authorId);
        return author.map(blogRepository::findAllByAuthor)
                .map(this::convertToFeedDto)
                .orElseThrow(() -> new UserNotFoundException("Author not found"));
    }

    // TODO: Write tests after follow functionality is added
    public FeedDto getFeedForUser(Long userId) {
        var userEntity = userRepository.findById(userId);
        return userEntity.map(UserEntity::getFollowing)
                .map(userEntities -> userEntities.stream()
                        .map(blogRepository::findAllByAuthor)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .map(this::convertToFeedDto)
                .orElseThrow(() -> new UserNotFoundException("User with input userId not found"));
    }

    private FeedDto convertToFeedDto(List<BlogEntity> blogEntities) {
        var blogDtoList = blogEntities
                .stream()
                .filter(blogEntity -> !blogEntity.isDeleted())
                .map(this::convertToBlogResponseDto)
                .collect(Collectors.toList());
        return FeedDto.builder()
                .blogListDto(blogDtoList)
                .build();
    }

    private BlogResponseDto convertToBlogResponseDto(BlogEntity blogEntity) {
        var userEntity = blogEntity.getAuthor();
        return BlogResponseDto.builder()
                .id(blogEntity.getId())
                .title(blogEntity.getTitle())
                .author(UserProfileResponseDto.builder()
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .email(userEntity.getEmail())
                        .bio(userEntity.getBio())
                        .build())
                .content(blogEntity.getContent())
                .summary(blogEntity.getSummary())
                .build();
    }
}