package com.example.service.services.imp;

import com.example.service.exception.MyBadRequestException;
import com.example.service.models.dtos.*;
import com.example.service.models.entities.RolesEntity;
import com.example.service.models.entities.UserEntity;
import com.example.service.repositories.LoginRespository;
import com.example.service.repositories.RoleRepository;
import com.example.service.repositories.UserRepository;
import com.example.service.services.JwtService;
import com.example.service.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRespository loginRespository;

    @Override
    @Transactional
    public UserInfoDto userinfo() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return modelMapper.map(user, UserInfoDto.class);
    }

    @Override
    @Transactional
    public TwoJwtDto login(LoginDto loginDto) {
        var authtoken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());
        try {
            UserDetailsDto user = (UserDetailsDto) authenticationManager.authenticate(authtoken).getPrincipal();
            UserEntity userE = userRepository.findByUsername(user.getUsername()).orElseThrow();
            String refreshtoken = jwtService.refreshToken(userE);
            String jwt = jwtService.token(user);
            return TwoJwtDto.builder().jwt(jwt).tokenrefresh(refreshtoken).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyBadRequestException("Username o password incorrectos");
        }

    }

    @Override
    @Transactional
    public TwoJwtDto register(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent())
            throw new MyBadRequestException("username ocupado");
        RolesEntity roles = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("No esixten roles"));

        UserEntity user = modelMapper.map(registerDto, UserEntity.class);
        user.setRoles(List.of(roles));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        var res = userRepository.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(res.getUsername());
        loginDto.setPassword(registerDto.getPassword());
        return login(loginDto);
    }

    @Override
    @Transactional
    public void logout(String refreshtoken) {
        try {
            RefreshDto refreshDto = jwtService.validationRefresh(refreshtoken);
            var loginentity = loginRespository.findById(refreshDto.getJwtid())
                    .orElseThrow(MyBadRequestException::new);
            loginentity.setState(false);
            loginRespository.save(loginentity);
        } catch (Exception e) {
            throw new MyBadRequestException("Inicie seccion de nuevo");
        }
    }

    @Override
    @Transactional
    public TokenDto refreshauth(String refreshtoken) {
        try {
            RefreshDto refreshDto = jwtService.validationRefresh(refreshtoken);
            var loginentity = loginRespository.findById(refreshDto.getJwtid())
                    .orElseThrow(MyBadRequestException::new);
            if (!loginentity.getState()) throw new MyBadRequestException();
            UserEntity user = userRepository.findByUsername(refreshDto.getUsername())
                    .orElseThrow(MyBadRequestException::new);
            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setUsername(user.getUsername());
            userDetailsDto.setNickname(user.getNickname());
            userDetailsDto.setAuthoritiesAsRoles(user.getRoles());
            String token = jwtService.token(userDetailsDto);
            return new TokenDto(token);
        } catch (Exception e) {
            throw new MyBadRequestException("Inicie seccion de nuevo");
        }
    }


}
