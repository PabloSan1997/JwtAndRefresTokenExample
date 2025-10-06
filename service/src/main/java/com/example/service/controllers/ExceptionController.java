package com.example.service.controllers;

import com.example.service.exception.MyBadRequestException;
import com.example.service.exception.NotRefreshException;
import com.example.service.exception.TokenExpireException;
import com.example.service.models.dtos.ErrorDto;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionController {

    private ResponseEntity<ErrorDto> generateError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorDto(status, message));
    }

    private String viewNotValidException(Exception e) {
        if (e instanceof MethodArgumentNotValidException err) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError field : err.getFieldErrors()) {
                stringBuilder.append(field.getField()).append(": ")
                        .append(field.getDefaultMessage()).append(". ");
            }
            return stringBuilder.toString().trim();
        }
        if (e instanceof TypeMismatchException err) {
            return "Error al convertir formado de dato, puede que se trate de un String que no se puede convertir a Long";
        }
        return e.getMessage();
    }

    @ExceptionHandler(NotRefreshException.class)
    public ResponseEntity<?> notRefresException(NotRefreshException e) {
        ResponseCookie cookie = ResponseCookie.from("mitoken", "")
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .build();
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorDto.getStatusCode())
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).body(errorDto);
    }

    @ExceptionHandler({
            MyBadRequestException.class,
            TypeMismatchException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> badrequest(Exception e) {
        String message = viewNotValidException(e);
        return generateError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler({
            TokenExpireException.class,
            AuthenticationException.class
    })
    public ResponseEntity<?> unauthorized(Exception e) {
        return generateError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> forbidden(AccessDeniedException e) {
        return generateError(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({
            NoResourceFoundException.class
    })
    public ResponseEntity<?> notFound(Exception e) {
        return generateError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerError(Exception e) {
        return generateError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
