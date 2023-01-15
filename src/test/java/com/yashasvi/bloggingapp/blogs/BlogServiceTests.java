package com.yashasvi.bloggingapp.blogs;

import com.yashasvi.bloggingapp.authentication.jsonwebtoken.JwtAuthenticationService;
import com.yashasvi.bloggingapp.blogs.dtos.BlogResponseDto;
import com.yashasvi.bloggingapp.blogs.dtos.CreateBlogRequestDto;
import com.yashasvi.bloggingapp.blogs.dtos.FeedDto;
import com.yashasvi.bloggingapp.blogs.summarizer.OpenNLPTextSummarizer;
import com.yashasvi.bloggingapp.users.UserRepository;
import com.yashasvi.bloggingapp.users.UserService;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
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
import static com.yashasvi.bloggingapp.TestUtils.INVALID_USER_ID;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD_1;
import static com.yashasvi.bloggingapp.TestUtils.TITLE;
import static com.yashasvi.bloggingapp.TestUtils.TITLE_1;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private UserProfileResponseDto user1;
    private UserProfileResponseDto user2;

    @BeforeEach
    void setup() {
        var textSummarizer = new OpenNLPTextSummarizer();
        blogService = new BlogService(blogRepository, userRepository, textSummarizer);
        var authenticationService = new JwtAuthenticationService(jwtSecret);
        var userService = new UserService(userRepository, authenticationService, new BCryptPasswordEncoder());
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        var registeredUser1 = userService.registerUser(registerUserRequestDto);
        var userEntity1 = userRepository.findById(registeredUser1.getUserId()).orElse(null);
        user1 = UserProfileResponseDto.builder()
                .id(userEntity1.getId())
                .username(userEntity1.getUsername())
                .email(userEntity1.getEmail())
                .bio(userEntity1.getBio())
                .build();
        var registerUserRequestDto1 = RegisterUserRequestDto.builder()
                .username(USERNAME_1)
                .email(EMAIL_1)
                .password(PASSWORD_1)
                .bio(BIO)
                .build();
        var registeredUser2 = userService.registerUser(registerUserRequestDto1);
        var userEntity2 = userRepository.findById(registeredUser2.getUserId()).orElse(null);
        user2 = UserProfileResponseDto.builder()
                .id(userEntity2.getId())
                .username(userEntity2.getUsername())
                .email(userEntity2.getEmail())
                .bio(userEntity2.getBio())
                .build();
    }

    @Test
    void test_createBlog_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(user1.getId(), createBlogRequestDto);
        assertEquals(TITLE, createBlogResponseDto.getTitle());
        assertEquals(user1, createBlogResponseDto.getAuthor());
        assertEquals(CONTENT, createBlogResponseDto.getContent());
    }

    @Test
    void test_createBlog_authorNotFound() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        assertThrows(UserNotFoundException.class,
                () -> blogService.createBlog(INVALID_USER_ID, createBlogRequestDto));
    }

    @Test
    void test_getGeneralFeed_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(user1.getId(), createBlogRequestDto);
        var createBlogRequestDto1 = CreateBlogRequestDto.builder()
                .title(TITLE_1)
                .content(CONTENT_1)
                .build();
        var createBlogResponseDto1 = blogService.createBlog(user2.getId(), createBlogRequestDto1);
        var actualResponse = blogService.getGeneralFeed();
        var expectedBlogDto = BlogResponseDto.builder()
                .id(createBlogResponseDto.getId())
                .title(TITLE)
                .author(user1)
                .content(CONTENT)
                .build();
        var expectedBlogDto1 = BlogResponseDto.builder()
                .id(createBlogResponseDto1.getId())
                .title(TITLE_1)
                .author(user2)
                .content(CONTENT_1)
                .build();
        var expectedResponse = FeedDto.builder()
                .blogListDto(List.of(expectedBlogDto, expectedBlogDto1))
                .build();

        compareFeedDto(expectedResponse, actualResponse);
    }

    @Test
    void test_getBlogsByAuthor_success() {
        var createBlogRequestDto = CreateBlogRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        var createBlogResponseDto = blogService.createBlog(user1.getId(), createBlogRequestDto);
        var createBlogRequestDto1 = CreateBlogRequestDto.builder()
                .title(TITLE_1)
                .content(CONTENT_1)
                .build();
        var createBlogResponseDto1 = blogService.createBlog(user1.getId(), createBlogRequestDto1);
        var actualResponse = blogService.getBlogsByAuthor(user1.getId());
        var expectedBlogDto = BlogResponseDto.builder()
                .id(createBlogResponseDto.getId())
                .title(TITLE)
                .author(user1)
                .content(CONTENT)
                .build();
        var expectedBlogDto1 = BlogResponseDto.builder()
                .id(createBlogResponseDto1.getId())
                .title(TITLE_1)
                .author(user1)
                .content(CONTENT_1)
                .build();
        var expectedResponse = FeedDto.builder()
                .blogListDto(List.of(expectedBlogDto, expectedBlogDto1))
                .build();
        compareFeedDto(actualResponse, expectedResponse);
    }

    @Test
    void test_getBlogsByAuthor_authorNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> blogService.getBlogsByAuthor(INVALID_USER_ID));
    }

    private void compareFeedDto(FeedDto actualResponse, FeedDto expectedResponse) {
        assertEquals(expectedResponse.getBlogListDto().size(), actualResponse.getBlogListDto().size());
        for (int i = 0; i < expectedResponse.getBlogListDto().size(); i++) {
            compareBlogDtoSkipSummary(expectedResponse.getBlogListDto().get(i), actualResponse.getBlogListDto().get(i));
        }
    }

    private void compareBlogDtoSkipSummary(BlogResponseDto expected, BlogResponseDto actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getContent(), actual.getContent());
    }
}
