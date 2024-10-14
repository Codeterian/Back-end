package com.codeterian.common.infrastructure.util;

import com.codeterian.common.infrastructure.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class TokenUtils {

    //도메인 서버에서 security filter를 사용하는 경우
    public Claims getClaimsByInternalToken(String internalToken, String internalSecretKey) {

        try {
            log.info("Util internalToken", internalToken);
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(internalSecretKey));
            return Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(internalToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException();
        }
    }

    public String passportToToken(Passport passport, String internalSecretKey) {

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(internalSecretKey));

        return Jwts.builder()
                .claim("userId", passport.getUserId())
                .claim("email", passport.getEmail())
                .claim("username", passport.getUserName())
                .claim("role", passport.getUserRole())
                .setExpiration(passport.getExpirationTime())
                .signWith(key)
                .compact();

    }

    //security filter를 사용하지 않는 경우 - 바로 Passport 객체로 변환
    public Passport toPassport(String internalToken, String internalSecretKey) {
        Claims claims = getClaimsByInternalToken(internalToken, internalSecretKey);
        validateClaims(claims);

        Passport passport = new Passport(claims.get("userId", Long.class), claims.get("email", String.class),
                claims.get("username", String.class), claims.getExpiration(), UserRole.valueOf(claims.get("role", String.class)));

        PassportContextHolder.setPassport(passport);
        return passport;
    }

    private void validateClaims(Claims claims) {
        log.info("Validating claims: {}", claims);
        if (claims.get("userId", Integer.class) == null) {
            throw new NullPointerException();
        }

        if (claims.get("email", String.class) == null) {
            throw new NullPointerException();
        }

        if (claims.get("username", String.class) == null) {
            throw new NullPointerException();
        }
        String userRole = claims.get("role", String.class);
        if (userRole == null) {
            throw new NullPointerException();
        }

        try {
            UserRole.valueOf(userRole);
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

}
