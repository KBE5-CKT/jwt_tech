package com.example.jwt.auth.dto;


import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
