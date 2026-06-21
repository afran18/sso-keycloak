package com.afran.keycloak_bff.controller;

import com.afran.keycloak_bff.dto.TokenResponse;
import com.afran.keycloak_bff.service.KeycloakAuthService;
import com.afran.keycloak_bff.service.SessionService;
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

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        System.out.println("===== LOGIN HIT =====");
        String url =
                "http://localhost:8080/realms/company-realm/protocol/openid-connect/auth"
                + "?client_id=app-a"
                + "&response_type=code"
                + "&scope=openid"
                + "&redirect_uri=http://localhost:8081/auth/callback";

        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws IOException{

//        return keycloakAuthService.exchangeCodeForTokens(code);
        System.out.println("===== CALLBACK HIT =====");
        System.out.println("Code = " + code);
        TokenResponse tokenResponse = keycloakAuthService.exchangeCodeForTokens(code);
        String sessionId = sessionService.createSession(tokenResponse);
        System.out.println("Session Created = " + sessionId);

        Cookie cookie = new Cookie("SESSION_ID", sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        response.sendRedirect("http://localhost:3000");
    }

    @GetMapping("/logout")
    public void logout(
            @CookieValue("SESSION_ID") String sessionId,
            HttpServletResponse response
    ) throws IOException {

        TokenResponse tokenResponse =
                sessionService.getSession(sessionId);

        String idToken = tokenResponse.getIdToken();

        sessionService.deleteSession(sessionId);

        Cookie cookie = new Cookie("SESSION_ID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        String logoutUrl =
                keycloakAuthService.buildLogoutUrl(idToken);

        response.sendRedirect(logoutUrl);
    }
}
