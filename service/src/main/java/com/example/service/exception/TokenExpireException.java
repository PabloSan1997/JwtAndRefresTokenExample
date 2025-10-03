package com.example.service.exception;

public class TokenExpireException extends RuntimeException{
    public TokenExpireException(){
        super("expiration");
    }
}
