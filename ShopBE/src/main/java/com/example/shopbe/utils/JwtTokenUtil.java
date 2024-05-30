package com.example.shopbe.utils;

import com.example.shopbe.exceptions.InvalidParamException;
import com.example.shopbe.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}") // get from application.properties
    private long expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws InvalidParamException {
        // properties -> clams
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());

        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // convert second to millisecond
                    .signWith(generateSignInKey(), SignatureAlgorithm.HS256)
                    .compact();

            return token;

        } catch (Exception e){

            throw new InvalidParamException("Can't generate token: " + e.getMessage());

        }

    }

    private Key generateSignInKey(){

        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(generateSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = this.extractClaims(token);

           return claimsResolver.apply(claims);
    }

    // check token expiration
    public boolean tokenExpired(String token) {

        Date expirationDate = this.extractClaim(token, Claims::getExpiration);

        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    // get phone number claim
    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // check
    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);

        return phoneNumber.equals(userDetails.getUsername()) && !tokenExpired(token);
    }

}
