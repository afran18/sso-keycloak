package com.afran.keycloak_bff_b.controller;

import com.afran.keycloak_bff_b.dto.TokenResponse;
import com.afran.keycloak_bff_b.dto.UserProfile;
import com.afran.keycloak_bff_b.service.JwtService;
import com.afran.keycloak_bff_b.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(
            @CookieValue(
                    value = "APP_B_SESSION",
                    required = false
            )
            String sessionId
    ) {

        if (sessionId == null) {

            System.out.println(
                    "[AUTH] No APP_B_SESSION cookie found"
            );

            return ResponseEntity.status(401).build();
        }

        System.out.println(
                "[AUTH] APP_B_SESSION cookie found"
        );

        TokenResponse token =
                sessionService.getSession(sessionId);

        if (token == null) {

            System.out.println(
                    "[AUTH] Session not found in Redis"
            );

            return ResponseEntity.status(401).build();
        }

        System.out.println(
                "[AUTH] Valid application session found"
        );

        return ResponseEntity.ok(
                jwtService.extractUserProfile(
                        token.getAccessToken()
                )
        );
    }
}
