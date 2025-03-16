package com.keniding.backend.auth.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
    private String token;
}
