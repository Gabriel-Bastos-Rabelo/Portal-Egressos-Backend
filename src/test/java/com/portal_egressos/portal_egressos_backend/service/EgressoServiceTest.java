package com.portal_egressos.portal_egressos_backend.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class EgressoServiceTest {
        @Autowired
        EgressoService egressoService;

        @Autowired
        EgressoRepository egressoRepositorio;

        @Test
        @Transactional
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
                                .status(Status.PENDENTE)
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
        @Transactional
        public void deveGerarErroAoTentarSalvarSemNome() {
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> egressoService.salvarEgresso(egresso),
                                "O nome do egresso deve ser informado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarSemEmail() {
                Usuario usuario = Usuario.builder()
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
                                .status(Status.PENDENTE)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> egressoService.salvarEgresso(egresso),
                                "O email do egresso deve ser informado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarSemSenha() {
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder().nome("teste")
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> egressoService.salvarEgresso(egresso),
                                "A senha do egresso deve ser informada.");
        }

        @Test
        @Transactional
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
                                .status(Status.PENDENTE)
                                .build();

                Egresso egresso2 = Egresso.builder().nome("Anderson Silva")
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario2)
                                .status(Status.PENDENTE)
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
        @Transactional
        public void deveAtualizarEgresso() {
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
                                .status(Status.PENDENTE)
                                .build();

                // ação
                Egresso egressoSalvo = egressoService.salvarEgresso(egresso);
                egressoSalvo.setNome("anderson");
                egressoSalvo.setDescricao("lorem lore lore ipsum ");
                egressoSalvo.setFoto("testeUrl");
                egressoSalvo.setLinkedin("https://www.linkedin.com/in/teste-891774237");
                egressoSalvo.setInstagram("https://www.instagram.com/teste.ts");
                egressoSalvo.setCurriculo("lorem lore lore ipsum");
                egressoSalvo.getUsuario().setEmail("anderson@gmail.com");
                egressoSalvo.getUsuario().setSenha("87654321");
                Egresso egressoRetornado = egressoRepositorio.save(egressoSalvo);

                // rollback
                egressoRepositorio.delete(egressoSalvo);
                egressoRepositorio.delete(egressoRetornado);

                // verificação
                Assertions.assertNotNull(egressoSalvo);
                Assertions.assertEquals(egressoSalvo.getNome(), egressoRetornado.getNome());
                Assertions.assertEquals(egressoSalvo.getDescricao(), egressoRetornado.getDescricao());
                Assertions.assertEquals(egressoSalvo.getFoto(), egressoRetornado.getFoto());
                Assertions.assertEquals(egressoSalvo.getLinkedin(), egressoRetornado.getLinkedin());
                Assertions.assertEquals(egressoSalvo.getInstagram(), egressoRetornado.getInstagram());
                Assertions.assertEquals(egressoSalvo.getCurriculo(), egressoRetornado.getCurriculo());
                Assertions.assertEquals(egressoSalvo.getUsuario().getEmail(), egressoRetornado.getUsuario().getEmail());
        }

        @Test
        @Transactional
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
                                .status(Status.PENDENTE)
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

        @Test
        @Transactional
        public void deveListarEgressos() {
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
                                .status(Status.PENDENTE)
                                .build();

                Egresso egresso2 = Egresso.builder().nome("Anderson Silva")
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario2)
                                .status(Status.PENDENTE)
                                .build();

                // acao
                Egresso egressoSalvo1 = egressoService.salvarEgresso(egresso1);
                Egresso egressoSalvo2 = egressoService.salvarEgresso(egresso2);
                List<Egresso> resultado = egressoService.listarEgressos();

                // rollback
                egressoRepositorio.delete(egressoSalvo1);
                egressoRepositorio.delete(egressoSalvo2);

                // verificações
                Assertions.assertEquals(2, resultado.size());
                Assertions.assertTrue(resultado.stream().anyMatch(e -> e.getNome().equals("Anderson Lopes")));
                Assertions.assertTrue(resultado.stream().anyMatch(e -> e.getNome().equals("Anderson Silva")));
        }

        @Test
        public void deveListarEgressosAprovados() {
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
                                .status(Status.APROVADO)
                                .build();

                Egresso egresso2 = Egresso.builder().nome("Anderson Silva")
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario2)
                                .status(Status.APROVADO)
                                .build();

                // acao
                Egresso egressoSalvo1 = egressoService.salvarEgresso(egresso1);
                Egresso egressoSalvo2 = egressoService.salvarEgresso(egresso2);
                List<Egresso> resultado = egressoService.listarEgressos();

                // rollback
                egressoRepositorio.delete(egressoSalvo1);
                egressoRepositorio.delete(egressoSalvo2);

                // verificações
                Assertions.assertEquals(2, resultado.size());
                resultado.forEach(n -> Assertions.assertEquals(Status.APROVADO, n.getStatus()));

        }

        @Test
        public void deveListarEgressosPendentes() {
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
                                .status(Status.PENDENTE)
                                .build();

                Egresso egresso2 = Egresso.builder().nome("Anderson Silva")
                                .descricao("lorem ipsum lore")
                                .foto("urlteste")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes-silva-891774237")
                                .instagram("https://www.instagram.com/andderson.ls")
                                .curriculo("lorem ipsum lore")
                                .usuario(usuario2)
                                .status(Status.PENDENTE)
                                .build();

                // acao
                Egresso egressoSalvo1 = egressoService.salvarEgresso(egresso1);
                Egresso egressoSalvo2 = egressoService.salvarEgresso(egresso2);
                List<Egresso> resultado = egressoService.listarEgressos();

                // rollback
                egressoRepositorio.delete(egressoSalvo1);
                egressoRepositorio.delete(egressoSalvo2);

                // verificações
                Assertions.assertEquals(2, resultado.size());
                resultado.forEach(n -> Assertions.assertEquals(Status.PENDENTE, n.getStatus()));

        }
}