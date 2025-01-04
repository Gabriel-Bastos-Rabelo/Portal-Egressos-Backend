package com.portal_egressos.portal_egressos_backend.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@SpringBootTest
@ActiveProfiles("test")
public class EgressoServiceTest {
    @Autowired
    EgressoService egressoService;

    @Autowired
    EgressoRepository egressoRepositorio;

    @Test
    public void deveSalvarEgresso() {
        // cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("12345678")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                .instagram("https://www.instagram.com/andderson.ls")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                .build();
        // acao
        Egresso egressoSalvo = egressoService.salvarEgresso(egresso);

        // rollback
        egressoRepositorio.delete(egressoSalvo);

        // verificação
        Assertions.assertNotNull(egressoSalvo);
        Assertions.assertNotNull(egressoSalvo.getId());
    }

    @Test
    public void deveBuscarEgresso() {
        // cenário
        Usuario usuario1 = Usuario.builder()
                .email("teste@teste.com")
                .senha("12345678")
                .role(UserRole.EGRESSO)
                .build();

        Usuario usuario2 = Usuario.builder()
                .email("teste2@teste2.com")
                .senha("12345678")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso1 = Egresso.builder().nome("Anderson Lopes")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                .instagram("https://www.instagram.com/andderson.ls")
                .curriculo("lorem ipsum lore")
                .usuario(usuario1)
                .build();

        Egresso egresso2 = Egresso.builder().nome("Anderson Silva")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                .instagram("https://www.instagram.com/andderson.ls")
                .curriculo("lorem ipsum lore")
                .usuario(usuario2)
                .build();

        Egresso filtro = Egresso.builder().nome("Anderson")
                .descricao(null)
                .foto(null)
                .linkedin(null)
                .instagram(null)
                .curriculo(null)
                .usuario(null)
                .build();

        // acao
        Egresso egressoSalvo1 = egressoService.salvarEgresso(egresso1);
        Egresso egressoSalvo2 = egressoService.salvarEgresso(egresso2);
        List<Egresso> resultado = egressoService.buscarEgresso(filtro);

        // rollback
        egressoRepositorio.delete(egressoSalvo1);
        egressoRepositorio.delete(egressoSalvo2);

        // verificação
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertTrue(resultado.stream().anyMatch(e -> e.getNome().equals("Anderson Lopes")));
        Assertions.assertTrue(resultado.stream().anyMatch(e -> e.getNome().equals("Anderson Silva")));
    }

    @Test
    public void deveRemoverEgresso() {
        // cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("12345678")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                .instagram("https://www.instagram.com/andderson.ls")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                .build();

        // ação
        Egresso egressoSalvo = egressoService.salvarEgresso(egresso);
        Long id = egressoSalvo.getId();
        egressoService.removerEgresso(egressoSalvo);
        Optional<Egresso> temp = egressoRepositorio.findById(id);

        // rollback
        egressoRepositorio.delete(egressoSalvo);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }
}