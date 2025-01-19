package com.portal_egressos.portal_egressos_backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.SignInDto;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@Import(TestSecurityConfig.class) // Se necessário
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignInSuccess() throws Exception {
        SignInDto signInDto = new SignInDto("gbastos@gmail.com", "12345678");

        Usuario mockUser = new Usuario(); 
        mockUser.setEmail("gbastos@gmail.com");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(tokenProvider.generateAccessToken(any(Usuario.class))).thenReturn("mocked-token");

        mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked-token"));
    }


    @Test
    void testSignInInvalidCredentials() throws Exception {
        SignInDto signInDto = new SignInDto("gbastos@gmail.com", "12345678");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.status").value(401));
    }

}
