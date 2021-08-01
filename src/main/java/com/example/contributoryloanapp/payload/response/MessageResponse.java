package com.example.contributoryloanapp.payload.response;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

public class MessageResponse extends RuntimeException{
    private String message;

    public MessageResponse(String message, HttpStatus status) {
        super(message);
    }

    public MessageResponse(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

