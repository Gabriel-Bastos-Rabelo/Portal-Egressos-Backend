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
            // Autentica o usuário
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var authUser = authenticationManager.authenticate(usernamePassword);

            // Gera o token de acesso
            var accessToken = tokenService.generateAccessToken((Usuario) authUser.getPrincipal());

            // Obtém o papel do usuário
            Usuario user = (Usuario) authUser.getPrincipal();
            String role = user.getRole().name(); // Obtém o papel do usuário (COORDENADOR ou EGRESSO)

            // Retorna o token de acesso e o papel do usuário
            return ResponseEntity.ok(new JwtDto(accessToken, role));
        } catch (AuthenticationException ex) {
            Map<String, Object> errorResponse = Map.of(
                    "message", "Credenciais inválidas",
                    "status", 401);
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

}
