package com.example.jwt.auth.service;


import com.example.jwt.auth.entity.User;
import com.example.jwt.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String email, String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        userRepository.save(User.builder()
                .email(email)
                .password(encoded)
                .role(User.UserRole.ROLE_USER)
                .build());
    }
}
