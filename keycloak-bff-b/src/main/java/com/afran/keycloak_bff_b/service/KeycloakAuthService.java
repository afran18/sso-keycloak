package com.afran.keycloak_bff_b.service;

import com.afran.keycloak_bff_b.config.KeycloakProperties;
import com.afran.keycloak_bff_b.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService {

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;

    public TokenResponse exchangeCodeForTokens(String code) {
        String tokenUrl = keycloakProperties.getAuthServerUrl()
                + "/realms/"
                + keycloakProperties.getRealm()
                + "/protocol/openid-connect/token";
        System.out.println("Client ID: " + keycloakProperties.getClientId());
        System.out.println("Client Secret: " + keycloakProperties.getClientSecret());

        return restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        "grant_type=authorization_code" +
                        "&client_id=" + keycloakProperties.getClientId() +
                                "&client_secret=" + keycloakProperties.getClientSecret() +
                                "&code=" + code +
                                "&redirect_uri=http://localhost:8082/auth/callback"
                )
                .retrieve()
                .body(TokenResponse.class);
    }

    public String buildLogoutUrl(String idToken) {

        return keycloakProperties.getAuthServerUrl()
                + "/realms/"
                + keycloakProperties.getRealm()
                + "/protocol/openid-connect/logout"
                + "?post_logout_redirect_uri=http://localhost:3001"
                + "&id_token_hint=" + idToken;
    }
}
