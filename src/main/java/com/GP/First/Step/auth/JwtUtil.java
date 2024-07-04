package com.GP.First.Step.auth;

import com.GP.First.Step.entities.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    // secret key for the signature in the JWT Token.
    private final String secret_key;
    // Token validity duration.
    private final long accessTokenValidity;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    // Constructor to set the values from properties file.
    public JwtUtil(@Value("${jwt.secret_key}") String secret_key,
                   @Value("${jwt.accessTokenValidity}") long accessTokenValidity) {
        this.secret_key = secret_key;
        this.accessTokenValidity = accessTokenValidity;
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }


    // Method to create a JWT token.
    // JWT Claim contains 3 parts,
    // Header(algo,typ), Payload (data of the user), and signature(secret key).
    public String createToken(Optional<User> user) {
        // check if the user present.
        if (user.isPresent()) {
            // setting payload to the token.
            User u = user.get();
            Claims claims = Jwts.claims().setSubject(u.getEmail());
            claims.put("firstName", u.getFirstName());
            claims.put("lastName", u.getLastName());
            Date tokenCreateTime = new Date();
            Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(tokenCreateTime) // set the token creation date.
                    .setExpiration(tokenValidity) // set the token expiration date.
                    .signWith(SignatureAlgorithm.HS256, secret_key) // setting the signature.
                    .compact();
        } else {
            throw new IllegalArgumentException("User is not present");
        }
    }

    // Method to parse JWT claims.
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    // Method to resolve claims from the HTTP request.
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    // Method to resolve the token from the HTTP request.
    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    // method to validate the token.
    public boolean isTokenValid(Claims claims, UserDetails userDetails) {
        final String username = claims.getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(claims));
    }

    // Method to validate the claims of the token.
    public boolean isTokenExpired(Claims claims) throws AuthenticationException {
        return claims.getExpiration().before(new Date());
    }

    // Method to get the email from the claims.
    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    // Method to get roles from the claims.
    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

}