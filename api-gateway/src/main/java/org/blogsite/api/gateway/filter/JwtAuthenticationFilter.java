package org.blogsite.api.gateway.filter;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String jwtSecret = "secret";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = extractJwtToken(request);

        try {
            if (jwtToken != null && validateJwtToken(jwtToken)) {
                Authentication authentication = getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean validateJwtToken(String jwtToken) throws Exception {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException
                 | UnsupportedJwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Authentication getAuthentication(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        String username = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
}
