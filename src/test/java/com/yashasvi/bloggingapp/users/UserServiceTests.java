package com.yashasvi.bloggingapp.users;

import com.yashasvi.bloggingapp.authentication.jsonwebtoken.JwtAuthenticationService;
import com.yashasvi.bloggingapp.users.dtos.LoginUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.exceptions.InvalidCredentialsException;
import com.yashasvi.bloggingapp.users.exceptions.UserAlreadyExists;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.yashasvi.bloggingapp.TestUtils.BIO;
import static com.yashasvi.bloggingapp.TestUtils.EMAIL;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD_1;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME_1;
import static com.yashasvi.bloggingapp.TestUtils.INVALID_USER_ID;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Value("${auth.jwt-secret}")
    private String jwtSecret;
    private UserService userService;

    @BeforeEach
    void setup() {
        var authenticationService = new JwtAuthenticationService(jwtSecret);
        userService = new UserService(userRepository, authenticationService, new BCryptPasswordEncoder());
    }

    @Test
    void test_registerUser_success() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        var actualResponse = userService.registerUser(registerUserRequestDto);
        Assertions.assertEquals(USERNAME, actualResponse.getUsername());
    }

    @Test
    void test_registerUser_userAlreadyExists() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        userService.registerUser(registerUserRequestDto);
        Assertions.assertThrows(UserAlreadyExists.class,
                () -> userService.registerUser(registerUserRequestDto)
        );
    }

    @Test
    void test_loginUser_success() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        userService.registerUser(registerUserRequestDto);
        var loginUserRequestDto = LoginUserRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        var actualResponse = userService.loginUser(loginUserRequestDto);
        Assertions.assertEquals(USERNAME, actualResponse.getUsername());
    }

    @Test
    void test_loginUser_userDoesntExist() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        userService.registerUser(registerUserRequestDto);
        var loginUserRequestDto = LoginUserRequestDto.builder()
                .username(USERNAME_1)
                .password(PASSWORD)
                .build();
        Assertions.assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginUserRequestDto)
        );
    }

    @Test
    void test_loginUser_incorrectPassword() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        userService.registerUser(registerUserRequestDto);
        var loginUserRequestDto = LoginUserRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD_1)
                .build();
        Assertions.assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginUserRequestDto)
        );
    }

    @Test
    void test_getUserProfile_success() {
        var registerUserRequestDto = RegisterUserRequestDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        var registeredUser = userService.registerUser(registerUserRequestDto);
        var actualResponse = userService.getUserProfile(registeredUser.getUserId());
        var expectedResponse = UserProfileResponseDto.builder()
                .id(registeredUser.getUserId())
                .username(USERNAME)
                .email(EMAIL)
                .bio(BIO)
                .build();
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void test_getUserProfile_userNotFound() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserProfile(INVALID_USER_ID));
    }
}
