package com.example.jwt.auth.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // String -> Enum 변경

    public enum UserRole {
        ROLE_USER,
        ROLE_ADMIN;

        public String toSpringRole() {
            return this.name().replace("ROLE_", "");
        }
    }
}
