package es.localchat.component.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateToken(String clientUUID);
    String extractUuid(String JWT);
}
