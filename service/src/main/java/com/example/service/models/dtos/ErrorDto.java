package com.example.service.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private Integer statusCode;
    private String error;
    private String message;

    public ErrorDto(HttpStatus status, String message){
        this.error = status.getReasonPhrase();
        this.statusCode = status.value();
        this.message = message;
    }
}
