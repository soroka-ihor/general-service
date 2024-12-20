package es.localchat.component.auth.service.impl;

import es.localchat.component.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceLogic implements JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String uuid = extractUuid(token);
        return uuid.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String generateToken(String userUuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", userUuid);
        return generateToken(claims, userUuid);
    }

    private String generateToken(Map<String, Object> extraClaims, String userUuid) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userUuid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        Date.from(
                                LocalDateTime.now().plusYears(1).toLocalDate()
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUuid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Fetching data from the token
     *
     * @param token jwtToken
     * @return data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Getting key for signing token
     *
     * @return key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
