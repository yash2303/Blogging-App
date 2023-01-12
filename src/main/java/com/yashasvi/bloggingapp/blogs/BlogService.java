package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.blogs.dtos.BlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.blogs.dtos.UpdateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.exceptions.BlogNotFoundException;
import com.yashasvi.bloggingapp.users.UserEntity;
import com.yashasvi.bloggingapp.users.UserRepository;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public BlogResponseDto createBlog(Long authorId, CreateBlogRequestDto createBlogRequestDto) {
        var userEntity = userRepository.findById(authorId);
        return userEntity.map(author -> BlogEntity.builder()
                        .author(author)
                        .title(createBlogRequestDto.getTitle())
                        .content(createBlogRequestDto.getContent())
                        .build())
                .map(blogRepository::save)
                .map(this::convertToBlogResponseDto)
                .orElseThrow(() -> new UserNotFoundException("Author not found"));
    }

    public BlogResponseDto updateBlog(Long blogId, UpdateBlogRequestDto updateBlogRequestDto) {
        var blogEntity = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with input blogId doesn't exist"));
        if (Objects.nonNull(updateBlogRequestDto.getTitle())) {
            blogEntity.setTitle(updateBlogRequestDto.getTitle());
        }
        if (Objects.nonNull(updateBlogRequestDto.getContent())) {
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

    // TODO: Add sorting and pagination logic
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
                        .toList())
                .map(this::convertToFeedDto)
                .orElseThrow(() -> new UserNotFoundException("User with input userId not found"));
    }

    private FeedDto convertToFeedDto(List<BlogEntity> blogEntities) {
        var blogDtoList = blogEntities
                .stream()
                .filter(blogEntity -> !blogEntity.isDeleted())
                .map(this::convertToBlogResponseDto)
                .toList();
        return FeedDto.builder()
                .blogListDto(blogDtoList)
                .build();
    }

    private BlogResponseDto convertToBlogResponseDto(BlogEntity blogEntity) {
        return BlogResponseDto.builder()
                .id(blogEntity.getId())
                .title(blogEntity.getTitle())
                .authorId(blogEntity.getId())
                .content(blogEntity.getContent())
                .build();
    }
}