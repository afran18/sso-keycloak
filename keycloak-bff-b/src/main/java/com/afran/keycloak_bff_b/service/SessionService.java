package com.afran.keycloak_bff_b.service;

import com.afran.keycloak_bff_b.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
private final RedisTemplate<String, Object> redisTemplate;

    public String createSession(
            TokenResponse tokenResponse
    ) {

        String sessionId =
                UUID.randomUUID().toString();

        redisTemplate.opsForValue()
                .set(
                        sessionId,
                        tokenResponse,
                        Duration.ofMinutes(5)
                );
        System.out.println(
                "[REDIS] Session stored : "
                        + sessionId
        );
        return sessionId;
    }

    public TokenResponse getSession(
            String sessionId
    ) {

        System.out.println(
                "[REDIS] Looking up session : "
                        + sessionId
        );

        TokenResponse token =
                (TokenResponse) redisTemplate
                        .opsForValue()
                        .get(sessionId);

        if (token == null) {

            System.out.println(
                    "[REDIS] Session NOT FOUND"
            );
            return null;
        }

        System.out.println(
                "[REDIS] Session FOUND"
        );
        return token;
    }

    public void deleteSession(
            String sessionId
    ) {

        redisTemplate.delete(sessionId);
        System.out.println(
                "[REDIS] Session deleted : "
                        + sessionId
        );
    }
}
