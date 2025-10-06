package com.example.service.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank
    @Size(max = 60)
    private String username;
    @NotBlank
    @Size(max = 600)
    private String password;
    @NotBlank
    @Size(max = 60)
    private String nickname;
}
