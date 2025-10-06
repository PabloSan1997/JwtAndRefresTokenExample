package com.example.service.controllers;


import com.example.service.models.dtos.LoginDto;
import com.example.service.models.dtos.RegisterDto;
import com.example.service.models.dtos.TokenDto;
import com.example.service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userinfo(){
        return ResponseEntity.ok(userService.userinfo());
    }

    @PostMapping("/refreshauth")
    public ResponseEntity<?> refresh(@CookieValue(name = "mitoken") String token){
        return ResponseEntity.ok(userService.refreshauth(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "mitoken") String token){
        userService.logout(token);
        ResponseCookie cookie = ResponseCookie.from("mitoken", "")
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .build();

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        var res = userService.login(loginDto);
        ResponseCookie cookie = ResponseCookie.from("mitoken", res.getTokenrefresh())
                .maxAge(60*60*24*7)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new TokenDto(res.getJwt()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> login(@Valid @RequestBody RegisterDto registerDto){
        var res = userService.register(registerDto);
        ResponseCookie cookie = ResponseCookie.from("mitoken", res.getTokenrefresh())
                .maxAge(60*60*24*7)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new TokenDto(res.getJwt()));
    }
}
