
package com.example.project.config.jwt;

import com.example.project.config.Constants;
import com.example.project.ennity.Role;
import com.example.project.ennity.User;
import com.example.project.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Autowired
    public UserRepository userRepository;
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

//    public String generateToken(AuthUserDetails userDetails) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + Constants.JWT_EXPIRATION * 1000);
//        return Jwts.builder()
//                .setIssuer("self")
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .setSubject(String.valueOf(userDetails.getUser().getUsername()))
//                .addClaims(toMap(userDetails.getUser()))
//                .signWith(SignatureAlgorithm.HS512,SECRET_KEY) // Sử dụng khóa an toàn HS512
//                .compact();
//    }
    public String generateTokens(String username) {
        User user = userRepository.findByUsername(username);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Constants.JWT_EXPIRATION * 1000);
        String s = Jwts.builder()
                .setIssuer("self")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setSubject(user.getUsername())
                .addClaims(toMap(user))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY) // Sử dụng khóa an toàn HS512
                .compact();
        return s;
    }

    private Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (Arrays.stream(Constants.ATTRIBUTIES_TO_TOKEN).anyMatch(field.getName()::equals)) {
                try {
                    if (field.getName().equals("role")) {
                        // Truy cập trực tiếp thuộc tính "role" từ đối tượng User
                        Role role = (Role) field.get(obj);
                        if (role != null) {
                            // Bạn có thể chọn "role_id" hoặc "role_name" tùy ý
                            map.put("roleName", role.getName_role());
                            // Hoặc map.put("role_name", role.getRoleName());
                        }
                    } else {
                        map.put(field.getName(), field.get(obj));
                    }
                } catch (Exception e) {
                    // Xử lý ngoại lệ (nếu cần)
                }
            }
        }
        return map;
    }
public boolean validateToken(String jwt){
    Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
    return true;

}

    public boolean validateTokens(String jwt, String id_user){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            String subject = claims.getSubject();
            return subject.equals(id_user);
        } catch (Exception e){
            return false;
        }

    }
    private String extractUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("sub", String.class);
        } catch (Exception e) {
            // Xử lý lỗi khi không thể trích xuất userId từ token
            // ...
        }
        return null;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}