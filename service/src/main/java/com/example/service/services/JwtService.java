package com.example.service.services;

import com.example.service.models.dtos.RefreshDto;
import com.example.service.models.dtos.UserDetailsDto;
import com.example.service.models.entities.UserEntity;

public interface JwtService {
    String token(UserDetailsDto userDetailsDto);
    String refreshToken(UserEntity user);
    UserDetailsDto validation(String token);
    RefreshDto validationRefresh(String token);
}
