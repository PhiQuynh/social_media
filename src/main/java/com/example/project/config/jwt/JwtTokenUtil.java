package com.example.project.config.jwt;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.project.config.Constants;
import com.example.project.ennity.Role;
import com.example.project.ennity.User;
import com.example.project.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtil {

    @Value("socialmedia")
    private String secret;
    byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
    @Autowired
    UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    public String generateUserNameToken(String username) {
        User user = userRepository.findByUsername(username);
        byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_EXPIRATION))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512)) // Sử dụng khóa an toàn HS512
                .compact();
    }



}