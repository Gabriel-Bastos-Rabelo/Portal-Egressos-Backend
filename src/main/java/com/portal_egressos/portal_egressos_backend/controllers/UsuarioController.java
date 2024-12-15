package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.JwtDto;
import com.portal_egressos.portal_egressos_backend.dto.SignInDto;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var authUser = authenticationManager.authenticate(usernamePassword);
            var accessToken = tokenService.generateAccessToken((Usuario) authUser.getPrincipal());
            return ResponseEntity.ok(new JwtDto(accessToken));
        } catch (AuthenticationException ex) {
            Map<String, Object> errorResponse = Map.of(
                "message", "Credenciais inválidas",
                "status", 401
            );
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

}
