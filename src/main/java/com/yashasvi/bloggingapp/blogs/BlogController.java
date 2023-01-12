package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.blogs.dtos.BlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.blogs.dtos.UpdateBlogRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogResponseDto> createBlog(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateBlogRequestDto createBlogRequestDto) {
        var blogResponseDto = blogService.createBlog(userId, createBlogRequestDto);
        return ResponseEntity.created(URI.create("/blogs/" + blogResponseDto.getId())).body(blogResponseDto);
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<BlogResponseDto> updateBlog(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long blogId,
            @RequestBody UpdateBlogRequestDto updateBlogRequestDto) {
        var blogResponseDto = blogService.updateBlog(blogId, updateBlogRequestDto);
        return ResponseEntity.created(URI.create("/blogs/" + blogResponseDto.getId())).body(blogResponseDto);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<FeedDto> getFeed() {
        FeedDto feedDto = blogService.getGeneralFeed();
        return ResponseEntity.ok(feedDto);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponseDto> getBlogById(@PathVariable Long blogId) {
        var blogDto = blogService.getBlogById(blogId);
        return ResponseEntity.ok(blogDto);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<FeedDto> getBlogByAuthor(@PathVariable Long authorId) {
        var feedDto = blogService.getBlogsByAuthor(authorId);
        return ResponseEntity.ok(feedDto);
    }
}
