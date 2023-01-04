package com.yashasvi.bloggingapp.authentication.serversidetoken;

import com.yashasvi.bloggingapp.authentication.exceptions.InvalidTokenException;
import com.yashasvi.bloggingapp.users.UserEntity;
import com.yashasvi.bloggingapp.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class SSTAuthenticationServiceTests {
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private UserRepository userRepository;
    private SSTAuthenticationService authenticationService;
    private UserEntity savedUserEntity;

    @BeforeEach
    void setup() {
        authenticationService = new SSTAuthenticationService(authTokenRepository);
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
        var token = authenticationService.createToken(savedUserEntity);
        var userId = authenticationService.getUserIdFromToken(token);
        Assertions.assertEquals(savedUserEntity.getId(), userId);
    }

    @Test
    void test_getUserIdFromToken_invalidToken() {
        Assertions.assertThrows(InvalidTokenException.class,
                () -> authenticationService.getUserIdFromToken("123e4567-e89b-12d3-a456-426614174000"));
    }
}
