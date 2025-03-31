package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.JwtDto;
import com.portal_egressos.portal_egressos_backend.dto.SignInDto;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.CoordenadorRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenService;

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired 
    private CoordenadorRepository coordenadorRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var authUser = authenticationManager.authenticate(usernamePassword);

            Usuario user = (Usuario) authUser.getPrincipal();

            var accessToken = tokenService.generateAccessToken(user);

            String role = user.getRole().name(); // COORDENADOR ou EGRESSO
            Long userId = user.getId();
            String email = user.getEmail();
            Long egressoId = null;
            Long coordId = null;

            if (role.equals("EGRESSO")) {

                Optional<Egresso> egressoOptional = egressoRepository.findByUsuarioId(userId);

                if (egressoOptional.isEmpty() || egressoOptional.get().getStatus() != Status.APROVADO) {
                    throw new BadCredentialsException("Usuário ainda não aprovado.");
                }
                if (egressoOptional.isPresent()) {
                    egressoId = egressoOptional.get().getId();
                }
            }

            if (role.equals("COORDENADOR")) {
                coordId = coordenadorRepository.findByUsuarioId(userId)
                    .map(c -> c.getId())
                    .orElse(null);
            }
            

            return ResponseEntity.ok(new JwtDto(accessToken, role,egressoId,email,coordId));

        } catch (AuthenticationException ex) {
            System.err.println(ex);
            // Retorna erro de autenticação
            Map<String, Object> errorResponse = Map.of(
                    "message", "Credenciais inválidas",
                    "status", 401
            );
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
