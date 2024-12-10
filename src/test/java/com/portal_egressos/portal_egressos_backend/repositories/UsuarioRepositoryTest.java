package com.portal_egressos.portal_egressos_backend.repositories;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveSalvarUsuario() {
        //cenário
        Usuario user = Usuario.builder().email("teste@teste.com")
                                        .senha("123")
                                        .build();

        //ação
        Usuario saved = repository.save(user);
        //verificação
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(user.getEmail(), saved.getEmail());
        Assertions.assertEquals(user.getSenha(), saved.getSenha());
    }

    
    @Test
    public void deveSalvarVariosUsuarios() {
        // cenário
        List<Usuario> users = new ArrayList<Usuario>();
        for (int i = 0; i < 3; i++) {
            users.add(
                    Usuario.builder().email("teste@teste.com")
                                        .senha("123")
                                        .build());
        }

        // ação
        List<Usuario> saved = repository.saveAll(users);
        repository.deleteAll(saved);

        // verificação
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(users.size(), saved.size());
        for (int i = 0; i < users.size(); i++) {
            Assertions.assertEquals(users.get(i).getEmail(), saved.get(i).getEmail());
        }
    }


    @Test
    public void deveAtualizarUsuario() {
        // cenário
        Usuario user = Usuario.builder().email("teste@teste.com")
                                        .senha("123")
                                        .build();

        // ação
        Usuario saved = repository.save(user);
        saved.setEmail("teste2@teste.com");
        saved.setSenha("testesenha");
        Usuario returned = repository.save(saved);

        // verificação
        Assertions.assertNotNull(returned);
        Assertions.assertEquals(saved.getId(), returned.getId());
        Assertions.assertEquals(saved.getEmail(), returned.getEmail());
        Assertions.assertEquals(saved.getSenha(), returned.getSenha());
    }


    @Test
    public void deveRemoverUsuario() {
        // cenário
        Usuario user = Usuario.builder().email("teste@teste.com")
                                        .senha("123")
                                        .build();

        // ação
        Usuario saved = repository.save(user);
        Long id = saved.getId();
        repository.deleteById(id);
        Optional<Usuario> temp = repository.findById(id);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }


    @Test
    public void deveObterEgressoPorEmail() {
        // cenário
        Usuario user = Usuario.builder().email("teste@teste.com")
                                        .senha("123")
                                        .build();

        // ação
        repository.save(user);
        Optional<Usuario> returned = repository.findByEmail(user.getEmail());

        // verificação
        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(user.getEmail(), returned.get().getEmail());
    }
}

