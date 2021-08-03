package com.example.contributoryloanapp.exception;


public class ResourceNotFoundException extends RuntimeException {
    //exception
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
