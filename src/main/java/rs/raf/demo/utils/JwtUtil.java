package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.mapper.PermissionMapper;
import rs.raf.demo.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${oauth.jwt.secret}")
    private String SECRET_KEY;

    private PermissionMapper permissionMapper;

    public JwtUtil(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Claims extractAllClaims(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }
        catch (Exception e) {
            return null;
        }
        return claims;
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        if(claims == null)
            return null;
        return claims.getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        if(claims == null)
            return null;
        try {
            return claims.get("roles", List.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token){
        Claims claims = extractAllClaims(token);
        if(claims == null)
            return true;
        return claims.getExpiration().before(new Date());
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public String generateToken(User user) {
        //expiration 1 year
        System.out.println("Generating token for user: " + user.getUsername());

        long jwtExpirationMs = 365L * 24 * 60 * 60 * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);


        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getPermissions().stream().map(permissionMapper::toString).toList())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
