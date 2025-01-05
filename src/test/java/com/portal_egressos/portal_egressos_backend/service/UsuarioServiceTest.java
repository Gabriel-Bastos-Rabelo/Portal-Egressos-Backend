package com.portal_egressos.portal_egressos_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.UsuarioService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioRepository repositorio;

    @Autowired
    UsuarioService service;

    @Test
    public void deveCarregarUsuarioExistente() {
        // cenário
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> service.loadUserByUsername("email nao existente"),
                "Conta não encontrada.");

    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarCarregarUsuarioInexistente() {
        // cenário
        Usuario usuario = Usuario.builder().email("teste@teste.com")
                .senha("12345678")
                .role(UserRole.EGRESSO)
                .build();

        Usuario usuarioSalvo = repositorio.save(usuario);

        // ação
        UserDetails salvo = service.loadUserByUsername("teste@teste.com");

        // rollback
        repositorio.delete(usuarioSalvo);

        // verificação
        Assertions.assertNotNull(salvo);
        Assertions.assertNotNull(salvo.getUsername());
        Assertions.assertEquals("teste@teste.com", salvo.getUsername());

    }
}
