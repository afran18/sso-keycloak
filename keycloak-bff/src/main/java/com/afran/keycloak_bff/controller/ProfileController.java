package com.afran.keycloak_bff.controller;

import com.afran.keycloak_bff.dto.TokenResponse;
import com.afran.keycloak_bff.dto.UserProfile;
import com.afran.keycloak_bff.service.JwtService;
import com.afran.keycloak_bff.service.SessionService;
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
            @CookieValue(value = "SESSION_ID", required = false)
            String sessionId
    ) {

        if (sessionId == null) {
            return ResponseEntity.status(401).build();
        }

        TokenResponse token =
                sessionService.getSession(sessionId);

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        UserProfile profile =
                jwtService.extractUserProfile(
                        token.getAccessToken()
                );

        return ResponseEntity.ok(profile);
    }
}
