package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.blogs.dtos.BlogDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.blogs.exceptions.BlogNotFoundException;
import com.yashasvi.bloggingapp.users.UserEntity;
import com.yashasvi.bloggingapp.users.UserRepository;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public CreateBlogResponseDto createBlog(Long authorId, CreateBlogRequestDto createBlogRequestDto) {
        var userEntity = userRepository.findById(authorId);
        return userEntity.map(author -> BlogEntity.builder()
                        .author(author)
                        .title(createBlogRequestDto.getTitle())
                        .content(createBlogRequestDto.getContent())
                        .build())
                .map(blogRepository::save)
                .map(savedBlog -> CreateBlogResponseDto.builder()
                        .id(savedBlog.getId())
                        .title(savedBlog.getTitle())
                        .authorId(savedBlog.getId())
                        .content(savedBlog.getContent())
                        .build())
                .orElseThrow(() -> new UserNotFoundException("Author not found"));
    }

    public BlogDto getBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .map(blogEntity -> BlogDto.builder()
                        .id(blogEntity.getId())
                        .title(blogEntity.getTitle())
                        .authorId(blogEntity.getAuthor().getId())
                        .content(blogEntity.getContent())
                        .build())
                .orElseThrow(() -> new BlogNotFoundException("Blog with input blogId doesn't exist"));
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
                .map(blogEntity -> BlogDto.builder()
                        .id(blogEntity.getId())
                        .title(blogEntity.getTitle())
                        .authorId(blogEntity.getAuthor().getId())
                        .content(blogEntity.getContent())
                        .build())
                .toList();
        return FeedDto.builder()
                .blogListDto(blogDtoList)
                .build();
    }
}