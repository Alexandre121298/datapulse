package com.example.datapulse.controllers;

import com.example.datapulse.auth.controllers.AuthController;
import com.example.datapulse.auth.security.DatapulseUserDetails;
import com.example.datapulse.auth.security.GoogleOidcUserService;
import com.example.datapulse.auth.services.RegistrationService;
import com.example.datapulse.config.security.SecurityConfig;
import com.example.datapulse.user.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AuthSecurityTest {

    private static final String EMAIL = "securite@example.com";
    private static final String PASSWORD = "UnePhraseDeTest!2026";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private GoogleOidcUserService googleOidcUserService;

    @BeforeEach
    void configureAccount() {
        UserEntity account = UserEntity.createWithPassword(
                EMAIL,
                passwordEncoder.encode(PASSWORD)
        );

        // Simule un identifiant normalement attribué par PostgreSQL.
        ReflectionTestUtils.setField(account, "id", 1L);

        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(new DatapulseUserDetails(account));
    }

    @Test
    void shouldRejectAnonymousAccessToCurrentUser() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectLoginWithoutCsrf() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectLoginWithInvalidCsrf() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf().useInvalidToken())
                        .param("email", EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectIncorrectPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .param("email", EMAIL)
                        .param("password", "UnMauvaisMotDePasse!2026"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLoginAndRestoreUserFromSession() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(get("/api/auth/me")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void shouldLogoutAndInvalidateSession() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/auth/logout")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(session.isInvalid()).isTrue();

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectLogoutWithoutCsrfAndKeepSession() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/auth/me")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    private MockHttpSession login() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .param("email", EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().isNoContent())
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) result.getRequest().getSession(false);

        assertThat(session).isNotNull();

        return session;
    }
}