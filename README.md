# Keycloak BFF (Backend for Frontend) POC

A Proof of Concept demonstrating authentication using **Keycloak**, **Spring Boot**, **React**, and **Redis** following the **Backend for Frontend (BFF)** pattern.

## Architecture

```text
React App
    ↓
Spring Boot BFF
    ↓
Redis Session Store
    ↓
Keycloak
```

## Features

- Authorization Code Flow with Keycloak
- Backend for Frontend (BFF) architecture
- Server-side session management
- Session storage in Redis
- HttpOnly session cookies
- User profile retrieval from JWT claims
- Application logout
- Keycloak logout integration
- Request logging filter

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- React (Vite)
- Redis
- Docker
- Keycloak

## Authentication Flow

1. User opens the React application.
2. React checks for an existing session via the BFF.
3. If no session exists, the user is redirected to Keycloak.
4. Keycloak authenticates the user and returns an authorization code.
5. The BFF exchanges the authorization code for tokens.
6. Tokens are stored server-side in Redis.
7. A secure HttpOnly session cookie is returned to the browser.
8. Subsequent requests use the session cookie instead of exposing tokens to the frontend.

## Current Status

Implemented:

- Keycloak authentication
- Authorization code exchange
- Redis-backed sessions
- Session validation
- JWT claim extraction
- Logout flow
- Request logging

Planned:

- Refresh token flow
- Multi-application SSO (App A ↔ App B)
- Session expiration handling
- Production-grade security enhancements

## Purpose

This project was built to understand and demonstrate modern authentication patterns using Keycloak and the BFF approach, where access and refresh tokens remain on the server while the frontend communicates using secure session cookies.
