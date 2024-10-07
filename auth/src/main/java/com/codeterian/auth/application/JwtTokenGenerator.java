package com.codeterian.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    private final SecretKey key;

    private final Long expirationTime;

    public JwtTokenGenerator(@Value("${sever.jwt.secret-key}") String key,
                             @Value("${sever.jwt.access-expiration}") Long expirationTime) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
        this.expirationTime = expirationTime;
    }

    public String createJwtToken(Long userId,String email,
                                 String username, Collection<GrantedAuthority> roles) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .claim("username", username)
                .claim("role", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(key)
                .compact();
    }

}
