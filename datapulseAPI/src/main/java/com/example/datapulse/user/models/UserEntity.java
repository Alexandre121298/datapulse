package com.example.datapulse.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "google_subject", unique = true, length = 255)
    private String googleSubject;

    private UserEntity(
            String email,
            String passwordHash,
            String googleSubject
    ) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.googleSubject = googleSubject;
    }

    public static UserEntity createWithPassword(
            String email,
            String passwordHash
    ) {
        return new UserEntity(email, passwordHash, null);
    }

    public static UserEntity createWithGoogle(
            String email,
            String googleSubject
    ) {
        return new UserEntity(email, null, googleSubject);
    }
}