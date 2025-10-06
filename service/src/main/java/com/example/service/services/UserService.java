package com.example.service.services;


import com.example.service.models.dtos.*;

public interface UserService {
    UserInfoDto userinfo();
    TwoJwtDto login(LoginDto loginDto);
    TwoJwtDto register(RegisterDto registerDto);
    void logout(String refreshtoken);
    TokenDto refreshauth(String refreshtoken);
}
