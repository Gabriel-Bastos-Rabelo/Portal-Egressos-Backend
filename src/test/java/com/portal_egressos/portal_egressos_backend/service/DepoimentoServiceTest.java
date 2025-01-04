package com.portal_egressos.portal_egressos_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;

import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.DepoimentoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.services.DepoimentoService;

@SpringBootTest
@ActiveProfiles("test")
public class DepoimentoServiceTest {

    @Autowired
    DepoimentoService depoimentoService;

    @Autowired
    DepoimentoRepository depoimentoRepositorio;

    @Autowired
    EgressoRepository egressoRepositorio;

    @Test
    public void deveSalvarDepoimento() {
         // Cenário
         Usuario usuario = Usuario.builder()
         .email("teste@teste.com")
         .senha("123456")
         .role(UserRole.EGRESSO)
         .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);
        
        Depoimento depoimento = Depoimento.builder()
                    .egresso(egressoSalvo)
                    .texto("Depoimento Teste.")
                    .data(LocalDate.now())
                    .build();

        //Ação
        Depoimento depoimentoSalvo = depoimentoService.salvarDepoimento(depoimento);

        // Verificação
        Assertions.assertNotNull(depoimentoSalvo);
        Assertions.assertNotNull(depoimentoSalvo.getId());

        // Rollback
        depoimentoRepositorio.delete(depoimentoSalvo);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    public void deveGerarErroAoSalvarSemTexto() {
         // Cenário
         Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);
        
        Depoimento depoimento = Depoimento.builder()
                    .egresso(egressoSalvo)
                    .data(LocalDate.now())
                    .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> depoimentoService.salvarDepoimento(depoimento),
        "O texto do depoimento deve ser infomado.");
    }
    @Test
    public void deveAtualizarDepoimento() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .texto("Depoimento Teste.")
                .data(LocalDate.now())
                .egresso(egressoSalvo)
                .build();

        Depoimento depoimentoSalvo = depoimentoService.salvarDepoimento(depoimento);

        // Ação
        depoimentoSalvo.setTexto("Depoimento atualizado.");
        Depoimento depoimentoAtualizado = depoimentoService.atualizarDepoimento(depoimentoSalvo);

        // Rollback
        depoimentoRepositorio.delete(depoimentoAtualizado);
        egressoRepositorio.delete(egressoSalvo);

        // Verificação
        Assertions.assertNotNull(depoimentoSalvo);
        Assertions.assertEquals(depoimentoSalvo.getTexto(), depoimentoAtualizado.getTexto());
    }

    @Test
    public void deveRemoverDepoimento() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .texto("Depoimento Teste.")
                .data(LocalDate.now())
                .egresso(egressoSalvo)
                .build();

        Depoimento depoimentoSalvo = depoimentoService.salvarDepoimento(depoimento);
 
        // Ação
        depoimentoService.removerDepoimento(depoimentoSalvo);
        Optional<Depoimento> depoimentoRemovido = depoimentoRepositorio.findById(depoimentoSalvo.getId());

        // Verificação
        Assertions.assertFalse(depoimentoRemovido.isPresent());

        // Rollback
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    public void deveListarDepoimentosPorDataDesc() {
        // Cenário
        List<Egresso> egressos = new ArrayList<>();
        List<Depoimento> depoimentosSalvos = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("12345")
                .role(UserRole.EGRESSO)
                .build();

            Egresso egresso = Egresso.builder()
                    .nome("Egresso")
                    .descricao("estudante de ciencia da computacao")
                    .usuario(usuario)
                    .build();
            egressos.add(egressoRepositorio.save(egresso));

            Depoimento depoimento = Depoimento.builder()
                    .texto("Depoimento do Egresso " + i + ".")
                    .data(LocalDate.now())
                    .egresso(egressos.get(i - 1))
                    .build();

            depoimentosSalvos.add(depoimentoRepositorio.save(depoimento));
        }

        // Ação
        List<Depoimento> depoimentos = depoimentoRepositorio.findAllByOrderByDataDesc();

        // Rollback
        depoimentosSalvos.forEach(depoimentoRepositorio::delete);
        egressos.forEach(egressoRepositorio::delete);

        // Verificação
        Assertions.assertNotNull(depoimentos);
        Assertions.assertEquals(3, depoimentos.size());
    }


    @Test
    public void deveGerarErroAoSalvarSegundoDepoimentoParaMesmoEgresso() {
        // Cenário
        Usuario usuario = Usuario.builder()
        .email("teste@teste.com")
        .senha("123456")
        .role(UserRole.EGRESSO)
        .build();

        Egresso egresso = Egresso.builder()
        .nome("Egresso")
        .descricao("estudante de ciencia da computacao")
        .usuario(usuario)
        .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento primeiroDepoimento = Depoimento.builder()
                .texto("Primeiro depoimento.")
                .data(LocalDate.now())
                .egresso(egressoSalvo)
                .build();

        Depoimento segundoDepoimento = Depoimento.builder()
                .texto("Segundo depoimento.")
                .data(LocalDate.now())
                .egresso(egressoSalvo)
                .build();

        // Ação
        depoimentoService.salvarDepoimento(primeiroDepoimento);

        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> {
            depoimentoService.salvarDepoimento(segundoDepoimento);
        }, "O egresso já possui um depoimento.");

        // Rollback
        depoimentoRepositorio.delete(primeiroDepoimento);
        egressoRepositorio.delete(egressoSalvo);
    }
}

