package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.authentication.jsonwebtoken.JwtAuthenticationService;
import com.yashasvi.bloggingapp.blogs.dtos.BlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.users.UserRepository;
import com.yashasvi.bloggingapp.users.UserService;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static com.yashasvi.bloggingapp.TestUtils.BIO;
import static com.yashasvi.bloggingapp.TestUtils.CONTENT;
import static com.yashasvi.bloggingapp.TestUtils.CONTENT_1;
import static com.yashasvi.bloggingapp.TestUtils.EMAIL;
import static com.yashasvi.bloggingapp.TestUtils.EMAIL_1;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD_1;
import static com.yashasvi.bloggingapp.TestUtils.TITLE;
import static com.yashasvi.bloggingapp.TestUtils.TITLE_1;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME_1;
import static com.yashasvi.bloggingapp.TestUtils.USER_ID_10;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlogServiceTests {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${auth.jwt-secret}")
    private String jwtSecret;
    private BlogService blogService;
    private Long userId1;
    private Long userId2;

    @BeforeEach
    void setup() {
        blogService = new BlogService(blogRepository, userRepository);
        var authenticationService = new JwtAuthenticationService(jwtSecret);
        var userService = new UserService(userRepository, authenticationService, new BCryptPasswordEncoder());
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        var registeredUser1 = userService.registerUser(registerUserRequestDto);
        userId1 = registeredUser1.getUserId();
        var registerUserRequestDto1 = RegisterUserRequestDto.builder()
                .username(USERNAME_1)
                .email(EMAIL_1)
                .password(PASSWORD_1)
                .bio(BIO)
                .build();
        var registeredUser2 = userService.registerUser(registerUserRequestDto1);
        userId2 = registeredUser2.getUserId();
    }

    @Test
    void test_createBlog_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(userId1, createBlogRequestDto);
        Assertions.assertEquals(TITLE, createBlogResponseDto.getTitle());
        Assertions.assertEquals(userId1, createBlogResponseDto.getAuthorId());
        Assertions.assertEquals(CONTENT, createBlogResponseDto.getContent());
    }

    @Test
    void test_createBlog_authorNotFound() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        Assertions.assertThrows(UserNotFoundException.class,
                () -> blogService.createBlog(USER_ID_10, createBlogRequestDto));
    }

    @Test
    void test_getGeneralFeed_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(userId1, createBlogRequestDto);
        var createBlogRequestDto1 = CreateBlogRequestDto.builder()
                .title(TITLE_1)
                .content(CONTENT_1)
                .build();
        var createBlogResponseDto1 = blogService.createBlog(userId2, createBlogRequestDto1);
        var actualResponse = blogService.getGeneralFeed();
        var expectedBlogDto = BlogResponseDto.builder()
                .id(createBlogResponseDto.getId())
                .title(TITLE)
                .authorId(userId1)
                .content(CONTENT)
                .build();
        var expectedBlogDto1 = BlogResponseDto.builder()
                .id(createBlogResponseDto1.getId())
                .title(TITLE_1)
                .authorId(userId2)
                .content(CONTENT_1)
                .build();
        var expectedResponse = FeedDto.builder()
                .blogListDto(List.of(expectedBlogDto, expectedBlogDto1))
                .build();
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void test_getBlogsByAuthor_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(userId1, createBlogRequestDto);
        var createBlogRequestDto1 = CreateBlogRequestDto.builder()
                .title(TITLE_1)
                .content(CONTENT_1)
                .build();
        var createBlogResponseDto1 = blogService.createBlog(userId1, createBlogRequestDto1);
        var actualResponse = blogService.getBlogsByAuthor(userId1);
        var expectedBlogDto = BlogResponseDto.builder()
                .id(createBlogResponseDto.getId())
                .title(TITLE)
                .authorId(userId1)
                .content(CONTENT)
                .build();
        var expectedBlogDto1 = BlogResponseDto.builder()
                .id(createBlogResponseDto1.getId())
                .title(TITLE_1)
                .authorId(userId1)
                .content(CONTENT_1)
                .build();
        var expectedResponse = FeedDto.builder()
                .blogListDto(List.of(expectedBlogDto, expectedBlogDto1))
                .build();
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void test_getBlogsByAuthor_authorNotFound() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> blogService.getBlogsByAuthor(USER_ID_10));
    }
}
