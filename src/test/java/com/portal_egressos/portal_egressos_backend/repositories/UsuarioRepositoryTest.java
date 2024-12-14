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
    UsuarioRepository repositorio;

    @Test
    public void deveSalvarUsuario() {
        // cenário
        Usuario usuario = Usuario.builder().email("teste@teste.com")
                .senha("123")
                .build();

        // ação
        Usuario usuarioSalvo = repositorio.save(usuario);

        // rollback
        repositorio.delete(usuarioSalvo);

        // verificação
        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(usuario.getEmail(), usuarioSalvo.getEmail());
        Assertions.assertEquals(usuario.getSenha(), usuarioSalvo.getSenha());
    }

    @Test
    public void deveSalvarVariosUsuarios() {
        // cenário
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (int i = 0; i < 3; i++) {
            usuarios.add(
                    Usuario.builder().email("teste@teste.com" + i)
                            .senha("123" + i)
                            .build());
        }

        // ação
        List<Usuario> usuarioSalvo = repositorio.saveAll(usuarios);

        // rollback
        repositorio.deleteAll(usuarioSalvo);

        // verificação
        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(usuarios.size(), usuarioSalvo.size());
        for (int i = 0; i < usuarios.size(); i++) {
            Assertions.assertEquals(usuarios.get(i).getEmail(), usuarioSalvo.get(i).getEmail());
        }
    }

    @Test
    public void deveAtualizarUsuario() {
        // cenário
        Usuario usuario = Usuario.builder().email("teste@teste.com")
                .senha("123")
                .build();

        // ação
        Usuario usuarioSalvo = repositorio.save(usuario);
        usuarioSalvo.setEmail("teste2@teste.com");
        usuarioSalvo.setSenha("testesenha");
        Usuario usuarioRetornado = repositorio.save(usuarioSalvo);

        // rollback
        repositorio.delete(usuarioSalvo);

        // verificação
        Assertions.assertNotNull(usuarioRetornado);
        Assertions.assertEquals(usuarioSalvo.getId(), usuarioRetornado.getId());
        Assertions.assertEquals(usuarioSalvo.getEmail(), usuarioRetornado.getEmail());
        Assertions.assertEquals(usuarioSalvo.getSenha(), usuarioRetornado.getSenha());
    }

    @Test
    public void deveRemoverUsuario() {
        // cenário
        Usuario usuario = Usuario.builder().email("teste@teste.com")
                .senha("123")
                .build();

        // ação
        Usuario usuarioSalvo = repositorio.save(usuario);
        Long id = usuarioSalvo.getId();
        repositorio.deleteById(id);
        Optional<Usuario> temp = repositorio.findById(id);

        // rollback
        repositorio.delete(usuarioSalvo);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    public void deveObterUsuarioPorEmail() {
        // cenário
        Usuario usuario = Usuario.builder().email("teste@teste.com")
                .senha("123")
                .build();

        // ação
        Usuario usuarioSalvo = repositorio.save(usuario);
        Optional<Usuario> usuarioRetornado = repositorio.findByEmail(usuario.getEmail());

        // rollback
        repositorio.delete(usuarioSalvo);

        // verificação
        Assertions.assertTrue(usuarioRetornado.isPresent());
        Assertions.assertEquals(usuario.getEmail(), usuarioRetornado.get().getEmail());
    }
}
