package com.example.datapulse.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDTO {

    private AuthDTO() {
    }

    public record RegisterRequest(

            @NotBlank(message = "L'email est obligatoire.")
            @Email(
                    regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
                    message = "L'adresse email doit être au format nom@domaine.extension."
            )
            @Size(max = 254, message = "L'email est trop long.")
            String email,

            @NotBlank(message = "Le mot de passe est obligatoire.")
            @Size(
                    min = 15,
                    max = 128,
                    message = "Le mot de passe doit contenir entre 15 et 128 caractères."
            )
            String password

    ) {
    }

    public record UserResponse(
            Long id,
            String email
    ) {
    }

    public record CsrfResponse(
            String headerName,
            String token
    ) {
    }
}