package org.blogsite.api.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenUtil {
    private String SECRET_KEY = "secret";
    public Boolean validateToken(String token) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new Exception("Token has Expired");
        }
    }
}
