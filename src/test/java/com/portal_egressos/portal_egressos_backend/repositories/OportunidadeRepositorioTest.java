package com.portal_egressos.portal_egressos_backend.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

@SpringBootTest
@ActiveProfiles("test")
public class OportunidadeRepositorioTest {

        @Autowired
        OportunidadeRepository oportunidadeRepositorio;

        @Autowired
        EgressoRepository egressoRepositorio;

        @Test
        @Transactional
        public void deveVerificarSalvarOportunidade() {
                // Construção
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
                Oportunidade oportunidadeSalva = oportunidadeRepositorio.save(oportunidade);

                // Rollback
                oportunidadeRepositorio.delete(oportunidadeSalva);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadeSalva);
                Assertions.assertEquals(oportunidade.getTitulo(), oportunidadeSalva.getTitulo());
                Assertions.assertEquals(oportunidade.getDescricao(), oportunidadeSalva.getDescricao());
                Assertions.assertEquals(oportunidade.getLocal(), oportunidadeSalva.getLocal());
                Assertions.assertEquals(oportunidade.getTipo(), oportunidadeSalva.getTipo());
                Assertions.assertEquals(oportunidade.getDataPublicacao(), oportunidadeSalva.getDataPublicacao());
                Assertions.assertEquals(oportunidade.getDataExpiracao(), oportunidadeSalva.getDataExpiracao());
                Assertions.assertEquals(oportunidade.getSalario(), oportunidadeSalva.getSalario());
                Assertions.assertEquals(oportunidade.getLink(), oportunidadeSalva.getLink());
                Assertions.assertEquals(oportunidade.getStatus(), oportunidadeSalva.getStatus());
                Assertions.assertEquals(oportunidade.getEgresso().getId(), oportunidadeSalva.getEgresso().getId());
        }

        @Test
        @Transactional
        public void deveAtualizarOportunidade() {
                // Construção
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

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .salario(BigDecimal.valueOf(6000))
                                .status(Status.APROVADO)
                                .build();

                Oportunidade oportunidadeSalva = oportunidadeRepositorio.save(oportunidade);

                // Ação
                oportunidadeSalva.setTitulo("Desenvolvedor Full Stack");
                oportunidadeSalva.setSalario(BigDecimal.valueOf(7000));
                Oportunidade oportunidadeRetornada = oportunidadeRepositorio.save(oportunidadeSalva);

                // Rollback
                oportunidadeRepositorio.delete(oportunidadeRetornada);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadeRetornada);
                Assertions.assertEquals("Desenvolvedor Full Stack", oportunidadeRetornada.getTitulo());
                Assertions.assertEquals(oportunidadeSalva.getSalario(), oportunidadeRetornada.getSalario());

                // Verificar que os outros campos permanecem inalterados
                Assertions.assertEquals(oportunidadeSalva.getDescricao(), oportunidadeRetornada.getDescricao());
                Assertions.assertEquals(oportunidadeSalva.getLocal(), oportunidadeRetornada.getLocal());
                Assertions.assertEquals(oportunidadeSalva.getTipo(), oportunidadeRetornada.getTipo());
                Assertions.assertEquals(oportunidadeSalva.getDataPublicacao(),
                                oportunidadeRetornada.getDataPublicacao());
                Assertions.assertEquals(oportunidadeSalva.getStatus(), oportunidadeRetornada.getStatus());
                Assertions.assertEquals(oportunidadeSalva.getEgresso().getId(),
                                oportunidadeRetornada.getEgresso().getId());
        }

        @Test
        @Transactional
        public void deveListarUsuariosEOportunidades() {
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
                List<Oportunidade> oportunidades = new ArrayList<>();

                for (int i = 0; i < usuarios.size(); i++) {
                        Egresso egresso = Egresso.builder()
                                        .nome("Egresso" + i)
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

                        for (int j = 0; j < 2; j++) {
                                Oportunidade oportunidade = Oportunidade.builder()
                                                .egresso(egressoSalvo)
                                                .titulo("Oportunidade " + j + " do egresso " + i)
                                                .descricao("Descrição da oportunidade " + j)
                                                .local("Local " + j)
                                                .tipo("Tipo " + j)
                                                .dataPublicacao(LocalDate.now().minusDays(j))
                                                .salario(BigDecimal.valueOf(3000 + j * 1000))
                                                .status(Status.APROVADO)
                                                .build();
                                Oportunidade oportunidadeSalva = oportunidadeRepositorio.save(oportunidade);
                                oportunidades.add(oportunidadeSalva);
                        }
                }

                // Ação
                List<Oportunidade> oportunidadesRetornadas = oportunidadeRepositorio.findAll();

                // Rollback
                oportunidadeRepositorio.deleteAll(oportunidades);
                egressoRepositorio.deleteAll(egressos);

                // Verificação
                Assertions.assertNotNull(oportunidadesRetornadas);
                Assertions.assertEquals(6, oportunidadesRetornadas.size());
        }

        @Test
        @Transactional
        public void deveRemoverOportunidade() {
                // Construção
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

                Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egressoSalvo)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .salario(BigDecimal.valueOf(6000))
                                .status(Status.APROVADO)
                                .build();

                Oportunidade oportunidadeSalva = oportunidadeRepositorio.save(oportunidade);

                // Ação
                oportunidadeRepositorio.deleteById(oportunidadeSalva.getId());
                Optional<Oportunidade> oportunidadeRetornada = oportunidadeRepositorio
                                .findById(oportunidadeSalva.getId());

                // Rollback
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertFalse(oportunidadeRetornada.isPresent());
        }

        @Test
        @Transactional
        public void deveBuscarPorTituloContendoPalavraChave() {
                // Construção
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
                List<Oportunidade> oportunidades = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                        oportunidades.add(Oportunidade.builder()
                                        .egresso(egressoSalvo)
                                        .titulo("Oportunidade " + i)
                                        .descricao("Descrição da oportunidade " + i)
                                        .local("Local " + i)
                                        .tipo("Tipo " + i)
                                        .dataPublicacao(LocalDate.now().minusDays(i))
                                        .salario(BigDecimal.valueOf(5000 + i * 1000))
                                        .status(Status.APROVADO)
                                        .build());
                }
                oportunidadeRepositorio.saveAll(oportunidades);

                // Ação
                List<Oportunidade> oportunidadesRetornadas = oportunidadeRepositorio
                                .findByTituloContaining("Oportunidade");

                // Rollback
                oportunidadeRepositorio.deleteAll(oportunidades);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadesRetornadas);
                Assertions.assertEquals(3, oportunidadesRetornadas.size());
        }

        @Test
        @Transactional
        public void deveListarOportunidadesOrdenadasPorDataPublicacao() {
                // Construção
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

                List<Oportunidade> oportunidades = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                        oportunidades.add(Oportunidade.builder()
                                        .egresso(egressoSalvo)
                                        .titulo("Oportunidade " + i)
                                        .descricao("Descrição da oportunidade " + i)
                                        .local("Local " + i)
                                        .tipo("Tipo " + i)
                                        .dataPublicacao(LocalDate.now().minusDays(i))
                                        .salario(BigDecimal.valueOf(5000 + i * 1000))
                                        .status(Status.APROVADO)
                                        .build());
                }
                oportunidadeRepositorio.saveAll(oportunidades);

                // Ação
                List<Oportunidade> oportunidadesRetornadas = oportunidadeRepositorio
                                .findAllByOrderByDataPublicacaoDesc();

                // Rollback
                oportunidadeRepositorio.deleteAll(oportunidades);
                egressoRepositorio.delete(egressoSalvo);

                // Verificação
                Assertions.assertNotNull(oportunidadesRetornadas);
                Assertions.assertEquals(3, oportunidadesRetornadas.size());
                Assertions.assertTrue(oportunidadesRetornadas.get(0).getDataPublicacao()
                                .isAfter(oportunidadesRetornadas.get(1).getDataPublicacao()));
        }
}
