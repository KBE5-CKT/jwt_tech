package com.example.jwt.auth.service;

import com.example.jwt.auth.dto.LoginRequest;
import com.example.jwt.auth.dto.LoginResponse;
import com.example.jwt.auth.entity.RefreshToken;
import com.example.jwt.auth.repository.RefreshTokenRepository;
import com.example.jwt.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(LoginRequest request) {

        // 스프링 시큐리티 인증 처리
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authInputToken);

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(request.getEmail(), "ROLE_USER");
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // DB에 refresh 토큰 저장 (이미 있으면 갱신함)
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(request.getEmail())
                        .token(refreshToken)
                        .build()
        );

        return new LoginResponse(accessToken, refreshToken);

    }

    public void logout(String authHeader) {
        String accessToken = jwtTokenProvider.resolveToken(authHeader);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        refreshTokenRepository.deleteById(email);
    }

    public LoginResponse refresh(String refreshToken) {
        // DB에서 refresh 토큰 찾기
        RefreshToken token = refreshTokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(refreshToken))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 리프레시 토큰"));

        String email = token.getEmail();

        // 새로운 access token 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(email, "ROLE_USER");

        return new LoginResponse(newAccessToken, refreshToken); // refresh token 은 그대로 반환
    }

}
