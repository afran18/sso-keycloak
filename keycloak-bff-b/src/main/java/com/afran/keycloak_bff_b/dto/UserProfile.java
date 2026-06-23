package com.afran.keycloak_bff_b.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {

    private String username;
    private String email;
    private String name;
}