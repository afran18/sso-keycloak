package com.afran.keycloak_bff_b.controller;

import com.afran.keycloak_bff_b.config.KeycloakProperties;
import com.afran.keycloak_bff_b.dto.TokenResponse;
import com.afran.keycloak_bff_b.service.KeycloakAuthService;
import com.afran.keycloak_bff_b.service.SessionService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;
    private final SessionService sessionService;
    private final KeycloakProperties keycloakProperties;
    @PostConstruct
    public void init() {
        System.out.println("=================================");
        System.out.println("APP-B STARTED");
        System.out.println("CLIENT ID = "
                + keycloakProperties.getClientId());
        System.out.println("=================================");
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response)
            throws IOException {

        System.out.println("===== LOGIN HIT =====");

        String url =
                "http://localhost:8080/realms/company-realm/protocol/openid-connect/auth"
                        + "?client_id=" + keycloakProperties.getClientId()
                        + "&response_type=code"
                        + "&scope=openid"
                        + "&redirect_uri=http://localhost:8082/auth/callback";

        System.out.println("[SSO] Redirecting user to Keycloak");
        System.out.println("[APP-B LOGIN]");
        System.out.println(url);

        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam String code,
            HttpServletResponse response
    ) throws IOException {

        System.out.println("===== CALLBACK HIT =====");

        System.out.println(
                "[SSO] Authorization code received from Keycloak"
        );

        System.out.println("Code = " + code);

        System.out.println(
                "[SSO] Exchanging authorization code for tokens"
        );

        TokenResponse tokenResponse =
                keycloakAuthService.exchangeCodeForTokens(code);

        System.out.println(
                "[SSO] Token exchange successful"
        );

        String sessionId =
                sessionService.createSession(tokenResponse);

        System.out.println(
                "[SSO] Application session created"
        );

        System.out.println(
                "Session Created = " + sessionId
        );

        Cookie cookie =
                new Cookie("APP_B_SESSION", sessionId);

        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        System.out.println(
                "[SSO] APP_B_SESSION cookie created"
        );

        response.sendRedirect("http://localhost:3001");
    }

    @GetMapping("/logout")
    public void logout(
            @CookieValue("APP_B_SESSION")
            String sessionId,
            HttpServletResponse response
    ) throws IOException {

        System.out.println("===== LOGOUT HIT =====");

        System.out.println(
                "[SSO] Fetching session from Redis"
        );

        TokenResponse tokenResponse =
                sessionService.getSession(sessionId);

        String idToken =
                tokenResponse.getIdToken();

        System.out.println(
                "[SSO] Removing session from Redis"
        );

        sessionService.deleteSession(sessionId);

        Cookie cookie =
                new Cookie("APP_B_SESSION", "");

        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        System.out.println(
                "[SSO] APP_B_SESSION cookie removed"
        );

        String logoutUrl =
                keycloakAuthService.buildLogoutUrl(idToken);

        System.out.println(
                "[SSO] Redirecting to Keycloak logout"
        );

        System.out.println(logoutUrl);

        response.sendRedirect(logoutUrl);
    }
}
