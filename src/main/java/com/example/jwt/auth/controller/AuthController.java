package com.example.jwt.auth.controller;


import com.example.jwt.auth.dto.LoginRequest;
import com.example.jwt.auth.dto.LoginResponse;
import com.example.jwt.auth.repository.RefreshTokenRepository;
import com.example.jwt.auth.service.AuthService;
import com.example.jwt.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenProvider.resolveToken(authHeader);
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        refreshTokenRepository.deleteById(email);  // DB에서 리프레시 토큰 삭제
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        LoginResponse response = authService.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }

}


