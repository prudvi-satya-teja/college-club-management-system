package com.project.aclub.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String password;
}