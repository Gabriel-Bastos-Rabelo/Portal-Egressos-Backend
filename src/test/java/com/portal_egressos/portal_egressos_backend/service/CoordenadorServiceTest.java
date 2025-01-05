package com.portal_egressos.portal_egressos_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.CoordenadorRepository;
import com.portal_egressos.portal_egressos_backend.repositories.CursoRepository;
import com.portal_egressos.portal_egressos_backend.services.CoordenadorService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorServiceTest {
        @Autowired
        CoordenadorService coordService;

        @Autowired
        CoordenadorRepository coordRepositorio;

        @Autowired
        CursoRepository cursoRepositorio;

        @Test
        @Transactional
        public void deveSalvarCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("teste")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();
                // ação
                Coordenador coordenadorSalvo = coordService.salvarCoordenador(coordenador);

                // rollback
                coordRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertNotNull(coordenadorSalvo);
                Assertions.assertNotNull(coordenadorSalvo.getId());
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarSemNome() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder()
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> coordService.salvarCoordenador(coordenador),
                                "O nome do coordenadoor deve ser informado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarSemEmail() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("teste")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> coordService.salvarCoordenador(coordenador),
                                "O email do coordenadoor deve ser informado.");
        }

        @Test
        @Transactional
        public void deveGerarErroAoTentarSalvarSemSenha() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("teste")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                Assertions.assertThrows(RegraNegocioRunTime.class, () -> coordService.salvarCoordenador(coordenador),
                                "A senha do coordenadoor deve ser informada.");
        }

        @Test
        @Transactional
        public void deveAtualizarCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("teste")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                // ação
                Coordenador coordenadorSalvo = coordService.salvarCoordenador(coordenador);
                coordenadorSalvo.setNome("Anderson");
                coordenadorSalvo.setDataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0));
                coordenadorSalvo.setAtivo(false);
                coordenadorSalvo.getUsuario().setEmail("teste2@teste.com");
                Coordenador coordRetornado = coordRepositorio.save(coordenadorSalvo);

                // rollback
                coordRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertNotNull(coordenadorSalvo);
                Assertions.assertEquals(coordenadorSalvo.getNome(), coordRetornado.getNome());
                Assertions.assertEquals(coordenadorSalvo.getDataCriacao(), coordRetornado.getDataCriacao());
                Assertions.assertEquals(coordenadorSalvo.getAtivo(), coordRetornado.getAtivo());
                Assertions.assertEquals(coordenadorSalvo.getUsuario().getEmail(),
                                coordRetornado.getUsuario().getEmail());
        }

        @Test
        @Transactional
        public void deveRemoverCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("teste")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                // ação
                Coordenador coordSalvo = coordService.salvarCoordenador(coordenador);
                Long id = coordSalvo.getId();
                coordService.removerCoordenador(coordSalvo);
                Optional<Coordenador> temp = coordRepositorio.findById(id);

                // rollback
                coordRepositorio.delete(coordSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertFalse(temp.isPresent());
        }

        @Test
        @Transactional
        public void deveListarCoordenadores() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Coordenador coordenador = Coordenador.builder().nome("Anderson Lopes")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(cursoSalvo)
                                .usuario(usuario)
                                .build();

                // ação
                Coordenador coordSalvo = coordService.salvarCoordenador(coordenador);
                List<Coordenador> resultado = coordService.listarCoordenadores();

                // rollback
                coordRepositorio.delete(coordSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificações
                Assertions.assertEquals(1, resultado.size());
                Assertions.assertTrue(resultado.stream().anyMatch(e -> e.getNome().equals("Anderson Lopes")));
        }
}