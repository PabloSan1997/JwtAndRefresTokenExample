package com.example.service.security.filter;

import com.example.service.exception.TokenExpireException;
import com.example.service.models.dtos.ErrorDto;
import com.example.service.models.dtos.UserDetailsDto;
import com.example.service.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtValidationFilter extends BasicAuthenticationFilter {
    private final JwtService jwtService;
    public JwtValidationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace("Bearer ", "");

        try{
            UserDetailsDto userDetailsDto = jwtService.validation(token);
            String username = userDetailsDto.getUsername();
            var authorities = userDetailsDto.getAuthorities();
            var authtoken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authtoken);
        }catch (TokenExpireException e){
            if(!e.getMessage().equals("expiration")){
                chain.doFilter(request, response);
                return;
            }
            var errordto = new ErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            response.setStatus(errordto.getStatusCode());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errordto));
        }catch (Exception e){
            chain.doFilter(request, response);
        }
    }
}
