package com.yashasvi.bloggingapp.users;

import com.yashasvi.bloggingapp.authentication.AuthenticationService;
import com.yashasvi.bloggingapp.users.dtos.LoginUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.dtos.UserResponseDto;
import com.yashasvi.bloggingapp.users.exceptions.InvalidCredentialsException;
import com.yashasvi.bloggingapp.users.exceptions.UserAlreadyExists;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       @Qualifier("JwtAuthenticationService") AuthenticationService authenticationService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        if (Objects.nonNull(userRepository.getByUsername(registerUserRequestDto.getUsername()))
                || Objects.nonNull(userRepository.getByEmail(registerUserRequestDto.getEmail()))) {
            throw new UserAlreadyExists("User with input email/username already exists");
        }
        var userEntity = UserEntity.builder()
                .username(registerUserRequestDto.getUsername())
                .email(registerUserRequestDto.getEmail())
                .password(passwordEncoder.encode(registerUserRequestDto.getPassword()))
                .bio(registerUserRequestDto.getBio())
                .build();
        var savedUser = userRepository.save(userEntity);
        var authToken = authenticationService.createToken(savedUser);
        return UserResponseDto.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .authToken(authToken)
                .build();
    }

    public UserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) {
        var userEntity = userRepository.getByUsername(loginUserRequestDto.getUsername());
        if (Objects.isNull(userEntity) ||
                !passwordEncoder.matches(loginUserRequestDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Input username or password is incorrect");
        }
        var authToken = authenticationService.createToken(userEntity);
        return UserResponseDto.builder()
                .userId(userEntity.getId())
                .username(userEntity.getUsername())
                .authToken(authToken)
                .build();
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        var userEntity = getUserById(userId);
        return UserProfileResponseDto.builder()
                .id(userId)
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .build();
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with input userId doesn't exist"));
    }
}
