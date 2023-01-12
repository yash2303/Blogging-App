package com.yashasvi.bloggingapp.users;

import com.yashasvi.bloggingapp.users.dtos.LoginUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.dtos.UserResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
        log.info("Register User called with {}", registerUserRequestDto);
        var userResponseDto = userService.registerUser(registerUserRequestDto);
        return ResponseEntity.created(URI.create("/users/profile/" + userResponseDto.getUserId()))
                .body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        var userResponseDto = userService.loginUser(loginUserRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        var userProfileResponseDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileResponseDto);
    }
}
