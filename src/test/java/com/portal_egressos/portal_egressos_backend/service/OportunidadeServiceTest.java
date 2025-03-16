package com.portal_egressos.portal_egressos_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.OportunidadeRepository;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class OportunidadeServiceTest {
        @Autowired
        OportunidadeService oportunidadeService;

        @Autowired
        OportunidadeRepository oportunidadeRepositorio;

        @Autowired
        EgressoRepository egressoRepositorio;

        @Test
        @Transactional
        public void deveSalvarOportunidade() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                // Ação
                Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com");

                // Rollback
                oportunidadeRepositorio.delete(oportunidadeSalva);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadeSalva);
                Assertions.assertNotNull(oportunidadeSalva.getId());
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarOportunidadeNulo() {
                Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

                Egresso egresso = Egresso.builder()
                                .nome("Egresso")
                                .descricao("estudante de ciencia da computacao")
                                .usuario(usuario)
                .status(Status.APROVADO)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);
                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class, () -> {
                        oportunidadeService.salvarOportunidade(null, "teste@teste.com");
                }, "A oportunidade não pode ser nula");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemTitulo() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "O Título da oportunidade desse ser Infomado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemDescricao() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();

                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "A Descrição da oportunidade desse ser Infomada.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemLocal() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();

                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "O local da oportunidade desse ser Infomado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemTipo() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();

                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "O Tipo da oportunidade desse ser Infomado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemDataPublicacao() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "A data da oportunidade desse ser Infomada.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoSalvarSemStatus() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .build();

                // Verificação
                Assertions.assertThrows(RegraNegocioRunTime.class,
                                () -> oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com"),
                                "O Status da oportunidade desse ser Infomado.");
        }

        @Test
        @Transactional
        public void deveAtualizarOportunidade() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com");

                // Ação
                oportunidadeSalva.setTitulo("Desenvolvedor Java");
                oportunidadeSalva.setDescricao("Atualização da vaga");
                oportunidadeSalva.setLocal("São Luís");
                oportunidadeSalva.setDataPublicacao(LocalDate.now());
                oportunidadeSalva.setDataExpiracao(LocalDate.now().plusDays(30));
                oportunidadeSalva.setSalario(BigDecimal.valueOf(2500));
                oportunidadeSalva.setLink("Link.atualizado.com");
                oportunidadeSalva.setStatus(Status.NAO_APROVADO);

                Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidadeSalva);

                // Rollback
                oportunidadeRepositorio.delete(oportunidadeSalva);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadeSalva);
                Assertions.assertEquals(oportunidadeSalva.getTitulo(), oportunidadeAtualizada.getTitulo());
                Assertions.assertEquals(oportunidadeSalva.getTitulo(), oportunidadeAtualizada.getTitulo());
                Assertions.assertEquals(oportunidadeSalva.getDescricao(), oportunidadeAtualizada.getDescricao());
                Assertions.assertEquals(oportunidadeSalva.getLocal(), oportunidadeAtualizada.getLocal());
                Assertions.assertEquals(oportunidadeSalva.getDataPublicacao(),
                                oportunidadeAtualizada.getDataPublicacao());
                Assertions.assertEquals(oportunidadeSalva.getDataExpiracao(),
                                oportunidadeAtualizada.getDataExpiracao());
                Assertions.assertEquals(oportunidadeSalva.getSalario(), oportunidadeAtualizada.getSalario());
                Assertions.assertEquals(oportunidadeSalva.getLink(), oportunidadeAtualizada.getLink());
                Assertions.assertEquals(oportunidadeSalva.getStatus(), oportunidadeAtualizada.getStatus());

        }

        @Test
        @Transactional
        public void deveRemoverOportunidade() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com");

                // ação
                oportunidadeService.removerOportunidade(oportunidadeSalva);
                Optional<Oportunidade> oportunidadeRemovida = oportunidadeRepositorio
                                .findById(oportunidadeSalva.getId());

                // Verificação
                Assertions.assertFalse(oportunidadeRemovida.isPresent());

                // Rollback
                egressoRepositorio.delete(egressoSalvo);
        }

        @Test
        @Transactional
        public void deveBuscarPorTitulo() {
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
.status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade, "teste@teste.com");

                // ação
                List<Oportunidade> oportunidades = oportunidadeService.buscarPorTitulo("Desenvolvedor Backend");

                // Rollback
                oportunidadeRepositorio.delete(oportunidadeSalva);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertFalse(oportunidades.isEmpty());
                Assertions.assertTrue(
                                oportunidades.stream().anyMatch(o -> o.getTitulo().equals("Desenvolvedor Backend")));
        }

        @Test
        @Transactional
        public void deveListarOportunidades() {
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
                                .status(Status.PENDENTE)
                                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade1 = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();

                Oportunidade oportunidade2 = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Frontend")
                                .descricao("Desenvolvimento em next.js")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(5000))
                                .link("link vaga")
                                .status(Status.APROVADO)
                                .build();
                oportunidadeRepositorio.save(oportunidade1);
                oportunidadeRepositorio.save(oportunidade2);

                // Ação
                List<Oportunidade> oportunidades = oportunidadeService.listarTodasOportunidadesOrdenadasPorData();

                // Rollback
                oportunidadeRepositorio.delete(oportunidade1);
                oportunidadeRepositorio.delete(oportunidade2);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidades);
                Assertions.assertFalse(oportunidades.isEmpty());
                Assertions.assertEquals(2, oportunidades.size());
        }

        @Test
        @Transactional
        public void deveListarOportunidadesAprovadasOrdenadasPorData() {
                // Cenário
                Usuario usuario = Usuario.builder()
                        .email("teste@teste.com")
                        .senha("123456")
                        .role(UserRole.EGRESSO)
                        .build();

                Egresso egresso = Egresso.builder()
                        .nome("Egresso Aprovado")
                        .descricao("Estudante de ciência da computação")
                        .usuario(usuario)
                        .status(Status.APROVADO)
                        .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade1 = Oportunidade.builder()
                        .egresso(egressoSalvo)
                        .titulo("Desenvolvedor Backend")
                        .descricao("Desenvolvimento em Java")
                        .local("Remoto")
                        .tipo("CLT")
                        .dataPublicacao(LocalDate.now())
                        .dataExpiracao(LocalDate.now().plusDays(30))
                        .salario(BigDecimal.valueOf(6000))
                        .link("link vaga backend")
                        .status(Status.APROVADO)
                        .build();

                Oportunidade oportunidade2 = Oportunidade.builder()
                        .egresso(egressoSalvo)
                        .titulo("Desenvolvedor Frontend")
                        .descricao("Desenvolvimento em React")
                        .local("Remoto")
                        .tipo("CLT")
                        .dataPublicacao(LocalDate.now().minusDays(1))
                        .dataExpiracao(LocalDate.now().plusDays(30))
                        .salario(BigDecimal.valueOf(5000))
                        .link("link vaga frontend")
                        .status(Status.APROVADO)
                        .build();

                oportunidadeRepositorio.save(oportunidade1);
                oportunidadeRepositorio.save(oportunidade2);

                // Ação
                List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesAprovadasOrdenadasPorData();

                // Rollback
                oportunidadeRepositorio.delete(oportunidade1);
                oportunidadeRepositorio.delete(oportunidade2);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidades);
                Assertions.assertFalse(oportunidades.isEmpty());
                Assertions.assertEquals(2, oportunidades.size());
                Assertions.assertTrue(oportunidades.get(0).getDataPublicacao().isAfter(oportunidades.get(1).getDataPublicacao()));
                oportunidades.forEach(n -> Assertions.assertEquals(Status.APROVADO, n.getStatus()));

        }

        @Test
        @Transactional
        public void deveListarOportunidadesPendentesOrdenadasPorData() {
                // Cenário
                Usuario usuario = Usuario.builder()
                        .email("teste2@teste.com")
                        .senha("123456")
                        .role(UserRole.EGRESSO)
                        .build();

                Egresso egresso = Egresso.builder()
                        .nome("Egresso Pendente")
                        .descricao("Estudante de engenharia de software")
                        .usuario(usuario)
                        .status(Status.PENDENTE)
                        .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                Oportunidade oportunidade1 = Oportunidade.builder()
                        .egresso(egressoSalvo)
                        .titulo("Analista de Dados")
                        .descricao("Análise de dados em Python")
                        .local("Híbrido")
                        .tipo("CLT")
                        .dataPublicacao(LocalDate.now().minusDays(2))
                        .dataExpiracao(LocalDate.now().plusDays(15))
                        .salario(BigDecimal.valueOf(7000))
                        .link("link vaga analista")
                        .status(Status.PENDENTE)
                        .build();

                Oportunidade oportunidade2 = Oportunidade.builder()
                        .egresso(egressoSalvo)
                        .titulo("Engenheiro de Software")
                        .descricao("Desenvolvimento em cloud")
                        .local("Presencial")
                        .tipo("PJ")
                        .dataPublicacao(LocalDate.now().minusDays(1))
                        .dataExpiracao(LocalDate.now().plusDays(20))
                        .salario(BigDecimal.valueOf(8000))
                        .link("link vaga engenheiro")
                        .status(Status.PENDENTE)
                        .build();

                oportunidadeRepositorio.save(oportunidade1);
                oportunidadeRepositorio.save(oportunidade2);

                // Ação
                List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesPendentesOrdenadasPorData();

                // Rollback
                oportunidadeRepositorio.delete(oportunidade1);
                oportunidadeRepositorio.delete(oportunidade2);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidades);
                Assertions.assertFalse(oportunidades.isEmpty());
                Assertions.assertEquals(2, oportunidades.size());
                Assertions.assertTrue(oportunidades.get(0).getDataPublicacao().isAfter(oportunidades.get(1).getDataPublicacao()));
                oportunidades.forEach(n -> Assertions.assertEquals(Status.PENDENTE, n.getStatus()));
        }



}