package com.yashasvi.bloggingapp.users;

import com.yashasvi.bloggingapp.users.dtos.LoginUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.RegisterUserRequestDto;
import com.yashasvi.bloggingapp.users.dtos.UserProfileResponseDto;
import com.yashasvi.bloggingapp.users.dtos.UserResponseDto;
import com.yashasvi.bloggingapp.users.exceptions.InvalidCredentialsException;
import com.yashasvi.bloggingapp.users.exceptions.UserAlreadyExists;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        return UserResponseDto.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .authToken(generateAuthToken(savedUser.getId()))
                .build();
    }

    public UserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) {
        var userEntity = userRepository.getByUsername(loginUserRequestDto.getUsername());
        if (Objects.isNull(userEntity) ||
                !passwordEncoder.matches(loginUserRequestDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Input username or password is incorrect");
        }
        return UserResponseDto.builder()
                .userId(userEntity.getId())
                .username(userEntity.getUsername())
                .authToken(generateAuthToken(userEntity.getId()))
                .build();
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        try {
            var userEntity = userRepository.getReferenceById(userId);
            return UserProfileResponseDto.builder()
                    .id(userId)
                    .username(userEntity.getUsername())
                    .email(userEntity.getEmail())
                    .bio(userEntity.getBio())
                    .build();
        } catch (EntityNotFoundException ex) {
            throw new UserNotFoundException("User with input userId doesn't exist");
        }
    }

    // TODO: Add auth token generation logic
    private String generateAuthToken(Long userId) {
        return "";
    }
}
