package com.example.datapulse.auth.controllers;

import com.example.datapulse.auth.dto.AuthDTO;
import com.example.datapulse.auth.security.DatapulseUserDetails;
import com.example.datapulse.auth.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTO.UserResponse> register(
            @Valid @RequestBody AuthDTO.RegisterRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationService.register(request));
    }

    @GetMapping("/csrf")
    public AuthDTO.CsrfResponse csrf(CsrfToken csrfToken) {
        return new AuthDTO.CsrfResponse(
                csrfToken.getHeaderName(),
                csrfToken.getToken()
        );
    }

    @GetMapping("/me")
    public AuthDTO.UserResponse me(
            @AuthenticationPrincipal DatapulseUserDetails user
    ) {
        return new AuthDTO.UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}