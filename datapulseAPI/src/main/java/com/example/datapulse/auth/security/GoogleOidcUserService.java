package com.example.datapulse.auth.security;

import com.example.datapulse.user.models.UserEntity;
import com.example.datapulse.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GoogleOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final OidcUserService delegate = new OidcUserService();

    public GoogleOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) {
        OidcUser googleUser = delegate.loadUser(request);

        String subject = googleUser.getSubject();
        String email = googleUser.getEmail();

        if (subject == null || subject.isBlank()
                || email == null || email.isBlank()
                || !Boolean.TRUE.equals(googleUser.getEmailVerified())) {
            throw authenticationError("invalid_google_identity");
        }

        UserEntity user = findOrCreateUser(
                subject,
                email.strip().toLowerCase(Locale.ROOT)
        );

        return new DatapulseGoogleUser(user, googleUser);
    }

    private UserEntity findOrCreateUser(String subject, String email) {
        var existingUser = userRepository.findByGoogleSubject(subject);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        if (userRepository.existsByEmail(email)) {
            throw authenticationError("email_already_registered");
        }

        try {
            return userRepository.saveAndFlush(
                    UserEntity.createWithGoogle(email, subject)
            );
        } catch (DataIntegrityViolationException exception) {
            // Une connexion simultanée a pu créer le même compte.
            var concurrentUser = userRepository.findByGoogleSubject(subject);

            if (concurrentUser.isPresent()) {
                return concurrentUser.get();
            }

            if (userRepository.existsByEmail(email)) {
                throw authenticationError("email_already_registered");
            }

            throw exception;
        }
    }

    private OAuth2AuthenticationException authenticationError(String code) {
        return new OAuth2AuthenticationException(new OAuth2Error(code));
    }
}