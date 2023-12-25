package com.example.financialtracker.security;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.UserResDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String JWT_SECRET;

    @Value("${jwt.expire.date}")
    private String JWT_EXPIRE;

    public String extractToken(HttpServletRequest request, String key) {
        try {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        } catch (NullPointerException ex) {
            return "";
        }
        return "";
    }

    public void validateToken(String token) {
        if (token == null || isTokenExpired(token)) {
            throw new CustomException("Invalid or Missing token", 403);
        }
    }

    private boolean isTokenExpired(String token) {
        return decodeToken(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String generateToken(UserResDto user) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(JWT_EXPIRE));
        Date updatedDate = calendar.getTime();

        Key key = new SecretKeySpec(JWT_SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject("auth token")
                .setIssuedAt(currentDate)
                .setExpiration(updatedDate)
                .signWith(key)
                .claim("user", user)
                .compact();
    }

    public Claims decodeToken(String token) {
        try {
            Key key = new SecretKeySpec(JWT_SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new CustomException("Token Expired !", 403);
        } catch (Exception ex) {
            throw new CustomException("Token Invalid !", 403);
        }
    }
}
