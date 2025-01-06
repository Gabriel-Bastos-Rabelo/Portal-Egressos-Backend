package com.portal_egressos.portal_egressos_backend.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class DepoimentoRepositoryTest {

        @Autowired
        DepoimentoRepository depoimentoRepositorio;

        @Autowired
        EgressoRepository egressoRepositorio;

        @Test
        @Transactional
        public void deveVerificarSalvarDepoimento() {
                // construção
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder()
                .nome("Sabryna")
                .descricao("estudante de ciencia da computacao")
                .foto("url foto")
                .linkedin("url linkedin")
                .instagram("url instagram")
                .curriculo("curriculo")
                .usuario(usuario)
                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .egresso(egressoSalvo)
                .texto("Depoimento Teste")
                .data(LocalDate.now())
                .status(Status.PENDENTE)
                .build();

                // ação
                Depoimento depoimentoSalvo = depoimentoRepositorio.save(depoimento);

                // rollback
                depoimentoRepositorio.delete(depoimentoSalvo);
                egressoRepositorio.delete(egressoSalvo);

        // Verificação
        Assertions.assertNotNull(depoimentoSalvo);
        Assertions.assertEquals(depoimento.getTexto(), depoimentoSalvo.getTexto());
        Assertions.assertEquals(depoimento.getData(), depoimentoSalvo.getData());
        Assertions.assertEquals(depoimento.getStatus(), depoimentoSalvo.getStatus());
        Assertions.assertEquals(depoimento.getEgresso().getId(), depoimentoSalvo.getEgresso().getId());

                // Verificar os dados do Egresso associado
                Assertions.assertEquals(egresso.getNome(), depoimentoSalvo.getEgresso().getNome());
                Assertions.assertEquals(egresso.getDescricao(), depoimentoSalvo.getEgresso().getDescricao());
                Assertions.assertEquals(egresso.getFoto(), depoimentoSalvo.getEgresso().getFoto());
                Assertions.assertEquals(egresso.getLinkedin(), depoimentoSalvo.getEgresso().getLinkedin());
                Assertions.assertEquals(egresso.getInstagram(), depoimentoSalvo.getEgresso().getInstagram());
                Assertions.assertEquals(egresso.getCurriculo(), depoimentoSalvo.getEgresso().getCurriculo());

        }

        @Test
        @Transactional
        public void deveAtualizarDepoimento() {
                // construção
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder()
                .nome("Sabryna")
                .descricao("estudante de ciencia da computacao")
                .foto("url foto")
                .linkedin("url linkedin")
                .instagram("url instagram")
                .curriculo("curriculo")
                .usuario(usuario)
                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .egresso(egressoSalvo)
                .texto("Depoimento Teste")
                .data(LocalDate.now())
                .status(Status.PENDENTE)
                .build();

                Depoimento depoimentoSalvo = depoimentoRepositorio.save(depoimento);

                // ação
                depoimentoSalvo.setTexto("Depoimento Atualizado");
                Depoimento depoimentoRetornado = depoimentoRepositorio.save(depoimentoSalvo);

                // rollback
                depoimentoRepositorio.delete(depoimentoRetornado);
                egressoRepositorio.delete(egressoSalvo);

        // verificação
        Assertions.assertNotNull(depoimentoRetornado);
        Assertions.assertEquals("Depoimento Atualizado", depoimentoRetornado.getTexto());
        Assertions.assertEquals(depoimento.getData(), depoimentoRetornado.getData());
        Assertions.assertEquals(depoimento.getStatus(), depoimentoRetornado.getStatus());
        Assertions.assertEquals(depoimento.getEgresso().getId(), depoimentoRetornado.getEgresso().getId());
    }

        @Test
        @Transactional
        public void deveListarUsuariosESeusDepoimentos() {
                // Construção
                List<Usuario> usuarios = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                        usuarios.add(Usuario.builder()
                                        .email("usuario" + i + "@teste.com")
                                        .senha("123456")
                                        .role(UserRole.EGRESSO)
                                        .build());
                }

                List<Egresso> egressos = new ArrayList<>();
                List<Depoimento> depoimentos = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {
            Egresso egresso = Egresso.builder()
                    .nome("Sabryna" + i)
                    .descricao("estudante de ciencia da computacao")
                    .foto("url foto")
                    .linkedin("url linkedin")
                    .instagram("url instagram")
                    .curriculo("curriculo")
                    .usuario(usuarios.get(i))
                    .status(Status.PENDENTE)
                    .build();

                        Egresso egressoSalvo = egressoRepositorio.save(egresso);
                        egressos.add(egressoSalvo);

            Depoimento depoimento = Depoimento.builder()
                    .egresso(egressoSalvo)
                    .texto("Depoimento do egresso " + i)
                    .data(LocalDate.now().minusDays(i))
                    .status(Status.PENDENTE)
                    .build();

                        Depoimento depoimentoSalvo = depoimentoRepositorio.save(depoimento);
                        depoimentos.add(depoimentoSalvo);
                }

                // Ação
                List<Depoimento> depoimentosRetornados = depoimentoRepositorio.findAllByOrderByDataDesc();

                // Rollback
                depoimentoRepositorio.deleteAll(depoimentos);
                egressoRepositorio.deleteAll(egressos);

                // Verificação
                Assertions.assertNotNull(depoimentosRetornados);
                Assertions.assertEquals(3, depoimentosRetornados.size());
        }

        @Test
        @Transactional
        public void deveRemoverDepoimento() {
                // construção
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder()
                .nome("Sabryna")
                .descricao("estudante de ciencia da computacao")
                .foto("url foto")
                .linkedin("url linkedin")
                .instagram("url instagram")
                .curriculo("curriculo")
                .usuario(usuario)
                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .egresso(egressoSalvo)
                .texto("Depoimento Teste")
                .data(LocalDate.now())
                .status(Status.PENDENTE)
                .build();

                Depoimento depoimentoSalvo = depoimentoRepositorio.save(depoimento);

                // ação
                depoimentoRepositorio.deleteById(depoimentoSalvo.getId());
                Optional<Depoimento> depoimentoRetornado = depoimentoRepositorio.findById(depoimentoSalvo.getId());

                // rollback
                egressoRepositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertFalse(depoimentoRetornado.isPresent());
        }

        @Test
        @Transactional
        public void deveListarDepoimentosOrdenadosPeloMaisRecente() {
                // construção
                List<Usuario> usuarios = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                        usuarios.add(Usuario.builder()
                                        .email("usuario" + i + "@teste.com")
                                        .senha("123456")
                                        .role(UserRole.EGRESSO)
                                        .build());
                }

                List<Egresso> egressos = new ArrayList<>();
                List<Depoimento> depoimentos = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {
            Egresso egresso = Egresso.builder()
                    .nome("Egresso " + i)
                    .descricao("Descrição do egresso " + i)
                    .foto("url foto")
                    .linkedin("url linkedin")
                    .instagram("url instagram")
                    .curriculo("curriculo")
                    .usuario(usuarios.get(i))
                    .status(Status.PENDENTE)
                    .build();

                        Egresso egressoSalvo = egressoRepositorio.save(egresso);
                        egressos.add(egressoSalvo);

            Depoimento depoimento = Depoimento.builder()
                    .egresso(egressoSalvo)
                    .texto("Depoimento do egresso " + i)
                    .data(LocalDate.now().minusDays(i))
                    .status(Status.PENDENTE)
                    .build();

                        Depoimento depoimentoRetornado = depoimentoRepositorio.save(depoimento);
                        depoimentos.add(depoimentoRetornado);
                }

                // ação
                List<Depoimento> depoimentosRetornados = depoimentoRepositorio.findAllByOrderByDataDesc();

                // rollback
                depoimentoRepositorio.deleteAll(depoimentos);
                egressoRepositorio.deleteAll(egressos);

                // verificação
                Assertions.assertNotNull(depoimentosRetornados);
                Assertions.assertEquals(3, depoimentosRetornados.size());
                Assertions.assertTrue(
                                depoimentosRetornados.get(0).getData().isAfter(depoimentosRetornados.get(1).getData()));

        }
}
