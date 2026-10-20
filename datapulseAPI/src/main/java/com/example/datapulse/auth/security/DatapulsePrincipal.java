package com.example.datapulse.auth.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface DatapulsePrincipal extends UserDetails {

    Long getId();
}