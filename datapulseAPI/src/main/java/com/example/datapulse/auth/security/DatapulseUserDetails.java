package com.example.datapulse.auth.security;

import com.example.datapulse.user.models.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;

public class DatapulseUserDetails extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;

    public DatapulseUserDetails(UserEntity user) {
        super(
                user.getEmail(),
                user.getPasswordHash(),
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );

        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}