package com.afran.keycloak_bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwksUri = "http://localhost:8080/realms/company-realm/protocol/openid-connect/certs";

        return NimbusJwtDecoder.withJwkSetUri(jwksUri)
                .build();
    }
}
