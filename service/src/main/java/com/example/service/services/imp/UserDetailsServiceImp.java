package com.example.service.services.imp;

import com.example.service.exception.MyBadRequestException;
import com.example.service.models.dtos.UserDetailsDto;
import com.example.service.models.entities.RolesEntity;
import com.example.service.models.entities.UserEntity;
import com.example.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(MyBadRequestException::new);
        String password = user.getPassword();
        List<RolesEntity> roles = user.getRoles();
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setUsername(username);
        userDetailsDto.setPassword(password);
        userDetailsDto.setAuthoritiesAsRoles(roles);
        return userDetailsDto;
    }
}
