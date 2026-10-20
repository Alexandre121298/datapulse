package com.example.datapulse.auth.security;

import com.example.datapulse.user.models.UserEntity;
import com.example.datapulse.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DatapulseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatapulseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null) {
            throw new UsernameNotFoundException(
                    "Identifiants invalides."
            );
        }

        String email = username.strip().toLowerCase(Locale.ROOT);

        UserEntity user = userRepository.findByEmail(email)
                .filter(account -> account.getPasswordHash() != null)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Identifiants invalides."
                ));

        return new DatapulseUserDetails(user);
    }
}