package com.example.service.services.imp;

import com.example.service.exception.TokenExpireException;
import com.example.service.models.dtos.RefreshDto;
import com.example.service.models.dtos.UserDetailsDto;
import com.example.service.models.entities.Logins;
import com.example.service.models.entities.UserEntity;
import com.example.service.repositories.LoginRespository;
import com.example.service.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtServiceImp implements JwtService {
    @Value("${jwt.key.token}")
    private String keytoken;
    @Value("${jwt.key.refresh}")
    private String keyrefresh;
    @Autowired
    private LoginRespository loginRespository;


    private SecretKey secretKey(String thekey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(thekey));
    }

    @Override
    public String token(UserDetailsDto userDetailsDto) {
        List<String> authoritesnames = userDetailsDto.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Claims claims = Jwts.claims().add("authorities", authoritesnames)
                .add("nickname", userDetailsDto.getNickname()).build();
        return Jwts.builder().signWith(secretKey(keytoken))
                .subject(userDetailsDto.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 7))
                .compact();
    }

    @Override
    @Transactional
    public String refreshToken(UserEntity user) {
        var newrefresh = loginRespository.save(Logins.builder().refreshtoken(UUID.randomUUID().toString()).user(user).build());
        Claims claims = Jwts.claims().add("jwtid", newrefresh.getId().toString()).build();
        String token = Jwts.builder().signWith(secretKey(keyrefresh))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 *60*60*24))
                .claims(claims)
                .subject(user.getUsername()).compact();
        newrefresh.setRefreshtoken(token);
        loginRespository.save(newrefresh);
        return token;
    }

    @Override
    public UserDetailsDto validation(String token) {

        try {
            Claims claims = Jwts.parser().verifyWith(secretKey(keytoken)).build()
                    .parseSignedClaims(token).getPayload();
            String nickname = (String) claims.get("nickname");
            @SuppressWarnings("unchecked")
            List<String> authoritiesname = (List<String>) claims.get("authorities");
            String username = claims.getSubject();
            var authorities = authoritiesname.stream().map(SimpleGrantedAuthority::new).toList();
            var res = new UserDetailsDto();
            res.setAuthorities(authorities);
            res.setNickname(nickname);
            res.setUsername(username);

            return res;
        } catch (ExpiredJwtException e) {
            throw new TokenExpireException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public RefreshDto validationRefresh(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey(keyrefresh)).build()
                .parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        UUID idjwt = UUID.fromString((String) claims.get("jwtid"));
        return RefreshDto.builder().username(username).jwtid(idjwt).build();
    }
}
