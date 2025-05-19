package com.example.jwt.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public String getMyInfo(Authentication authentication) {
        // JwtAuthenticationFilter에서 SecurityContext에 등록한 Authentication 객체를 활용
        String email = authentication.getName();
        return "현재 로그인한 사용자: " + email;
    }
}
