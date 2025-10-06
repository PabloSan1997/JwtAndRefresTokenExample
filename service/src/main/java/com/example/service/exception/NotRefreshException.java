package com.example.service.exception;

public class NotRefreshException extends RuntimeException{
    public NotRefreshException() {
        super("expiro refreshtoken");
    }
}
