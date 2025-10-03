package com.example.service.models.dtos;

import com.example.service.models.entities.RolesEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
public class UserDetailsDto implements UserDetails {

    private String username;
    private String password;
    @Getter
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setAuthoritiesAsRoles(List<RolesEntity> roles) {
        authorities = roles.stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.getName()))
                .toList();
    }
}
