package com.afran.keycloak_bff.service;

import com.afran.keycloak_bff.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtDecoder jwtDecoder;

    public Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }

    public UserProfile extractUserProfile(String accessToken) {
        Jwt jwt = decode(accessToken);

        return UserProfile.builder()
                .username(jwt.getClaimAsString("preferred_username"))
                .email(jwt.getClaimAsString("email"))
                .name(jwt.getClaimAsString("name"))
                .build();
    }
}