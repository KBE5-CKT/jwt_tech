package com.example.jwt.auth.repository;

import com.example.jwt.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);
    Optional<RefreshToken> findByToken(String token);
}
