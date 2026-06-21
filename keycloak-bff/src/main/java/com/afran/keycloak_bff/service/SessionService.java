package com.afran.keycloak_bff.service;

import com.afran.keycloak_bff.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
//    private final Map<String, TokenResponse> sessions = new ConcurrentHashMap<>();
private final RedisTemplate<String, Object> redisTemplate;

    public String createSession(TokenResponse tokenResponse) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue()
                .set(
                        sessionId,
                        tokenResponse,
                        Duration.ofMinutes(5)
                );
        return sessionId;
    }

    public TokenResponse getSession(String sessionId) {
        System.out.println("---- Retreiving Session ----");
        return (TokenResponse)
                redisTemplate.opsForValue()
                        .get(sessionId);
    }

    public void deleteSession(String sessionId) {

        redisTemplate.delete(sessionId);
    }
}
