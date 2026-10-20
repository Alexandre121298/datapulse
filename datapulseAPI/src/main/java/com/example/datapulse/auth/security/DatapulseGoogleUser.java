package com.example.datapulse.auth.security;

import com.example.datapulse.user.models.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serial;

public class DatapulseGoogleUser
        extends DefaultOidcUser
        implements DatapulsePrincipal {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String email;

    public DatapulseGoogleUser(UserEntity user, OidcUser googleUser) {
        super(
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                googleUser.getIdToken(),
                googleUser.getUserInfo()
        );

        this.id = user.getId();
        this.email = user.getEmail();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }
}