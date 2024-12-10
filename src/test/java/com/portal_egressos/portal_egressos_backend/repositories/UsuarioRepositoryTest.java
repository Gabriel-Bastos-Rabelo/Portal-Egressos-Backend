package com.portal_egressos.portal_egressos_backend.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Usuario;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveVerificarSalvarUsuario() {
        //cenário
        Usuario user = Usuario.builder().email("teste@teste.com")
                                        .senha("123").build();

        //ação
        Usuario salvo = repository.save(user);
        //verificação
        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(user.getEmail(), salvo.getEmail());
        Assertions.assertEquals(user.getSenha(), salvo.getSenha());

        
    }
}

