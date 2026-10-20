package com.example.datapulse.auth.services;

import com.example.datapulse.auth.dto.AuthDTO;
import com.example.datapulse.common.exception.ConflictException;
import com.example.datapulse.user.models.UserEntity;
import com.example.datapulse.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthDTO.UserResponse register(AuthDTO.RegisterRequest request) {
        String email = request.email()
                .strip()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    "Un compte utilise déjà cette adresse email."
            );
        }

        String passwordHash = passwordEncoder.encode(request.password());

        UserEntity user = UserEntity.createWithPassword(
                email,
                passwordHash
        );

        UserEntity savedUser;

        try {
            savedUser = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            if (userRepository.existsByEmail(email)) {
                throw new ConflictException(
                        "Un compte utilise déjà cette adresse email."
                );
            }

            throw exception;
        }

        return new AuthDTO.UserResponse(
                savedUser.getId(),
                savedUser.getEmail()
        );
    }
}