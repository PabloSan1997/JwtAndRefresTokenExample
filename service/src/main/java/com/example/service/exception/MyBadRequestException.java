package com.example.service.exception;

public class MyBadRequestException extends RuntimeException{
    public MyBadRequestException() {
        super();
    }

    public MyBadRequestException(String message) {
        super(message);
    }
}
