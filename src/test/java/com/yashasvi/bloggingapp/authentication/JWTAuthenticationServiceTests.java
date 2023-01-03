package com.yashasvi.bloggingapp.authentication;


import com.yashasvi.bloggingapp.authentication.exceptions.InvalidTokenException;
import com.yashasvi.bloggingapp.authentication.jsonwebtoken.JWTAuthenticationService;
import com.yashasvi.bloggingapp.users.UserEntity;
import com.yashasvi.bloggingapp.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.yashasvi.bloggingapp.TestUtils.BIO;
import static com.yashasvi.bloggingapp.TestUtils.EMAIL;
import static com.yashasvi.bloggingapp.TestUtils.PASSWORD;
import static com.yashasvi.bloggingapp.TestUtils.USERNAME;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JWTAuthenticationServiceTests {
    @Autowired
    private UserRepository userRepository;
    private JWTAuthenticationService jwtAuthenticationService;
    private UserEntity savedUserEntity;

    @BeforeAll
    void setup() {
        jwtAuthenticationService = new JWTAuthenticationService("bweifubgvsdnf4wg");
        var userEntity = UserEntity.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        savedUserEntity = userRepository.save(userEntity);
    }

    @Test
    void test_createToken_authenticateToken() {
        var token = jwtAuthenticationService.createToken(savedUserEntity);
        var userId = jwtAuthenticationService.getUserIdFromToken(token);
        Assertions.assertEquals(savedUserEntity.getId(), userId);
    }

    @Test
    void test_createToken_withNullUserId() {
        var userEntity = UserEntity.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .bio(BIO)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtAuthenticationService.createToken(userEntity));
    }

    @Test
    void test_getUserIdFromToken_invalidToken() {
        Assertions.assertThrows(InvalidTokenException.class,
                () -> jwtAuthenticationService.getUserIdFromToken("invalid.jwt.token"));
    }
}
