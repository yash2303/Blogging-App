package com.yashasvi.bloggingapp.blogs.exceptions;

public class UserNotAuthorisedException extends RuntimeException {
    public UserNotAuthorisedException(String message) {
        super(message);
    }
}
