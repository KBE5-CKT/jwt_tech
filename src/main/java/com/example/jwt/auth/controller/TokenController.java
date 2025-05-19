package com.example.jwt.auth.controller;

import com.example.jwt.auth.dto.TokenResponse;
import com.example.jwt.auth.entity.RefreshToken;
import com.example.jwt.auth.repository.RefreshTokenRepository;
import com.example.jwt.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = jwtTokenProvider.resolveToken(authHeader);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("저장된 리프레시 토큰 없음"));

        if (!stored.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email, "ROLE_USER");
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        stored.setToken(newRefreshToken);
        refreshTokenRepository.save(stored);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }
}
