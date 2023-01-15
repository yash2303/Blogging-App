package com.yashasvi.bloggingapp.common;

import com.yashasvi.bloggingapp.blogs.exceptions.BlogNotFoundException;
import com.yashasvi.bloggingapp.blogs.exceptions.UserNotAuthorisedException;
import com.yashasvi.bloggingapp.users.exceptions.InvalidCredentialsException;
import com.yashasvi.bloggingapp.users.exceptions.UserAlreadyExists;
import com.yashasvi.bloggingapp.users.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({BlogNotFoundException.class, UserNotFoundException.class})
    protected ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(RuntimeException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({InvalidCredentialsException.class, UserNotAuthorisedException.class})
    protected ResponseEntity<ErrorResponseDto> handleUnauthorisedExceptions(RuntimeException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExists.class})
    protected ResponseEntity<ErrorResponseDto> handleDefaultExceptions(RuntimeException e, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(e.getMessage()));
    }
}