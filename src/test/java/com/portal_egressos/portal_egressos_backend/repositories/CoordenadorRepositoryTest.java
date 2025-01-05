package com.portal_egressos.portal_egressos_backend.repositories;

import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;

@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorRepositoryTest {

        @Autowired
        CoordenadorRepository coordenadorRepositorio;

        @Autowired
        CursoRepository cursoRepositorio;

        @Test
        @Transactional
        public void deveSalvarCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
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
                Coordenador coordenadorSalvo = coordenadorRepositorio.save(coordenador);

                // rollback
                coordenadorRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertNotNull(coordenadorSalvo);
                Assertions.assertEquals(coordenador.getNome(), coordenadorSalvo.getNome());
                Assertions.assertEquals(coordenador.getDataCriacao(), coordenadorSalvo.getDataCriacao());
                Assertions.assertEquals(coordenador.getAtivo(), coordenadorSalvo.getAtivo());
        }

        @Test
        @Transactional
        public void deveAtualizarCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
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
                Coordenador coordenadorSalvo = coordenadorRepositorio.save(coordenador);
                coordenadorSalvo.setNome("teste2@teste.com");
                coordenadorSalvo.setDataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0));
                coordenadorSalvo.setAtivo(false);
                Coordenador returnedCoordenador = coordenadorRepositorio.save(coordenadorSalvo);

                // rollback
                coordenadorRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertNotNull(coordenadorSalvo);
                Assertions.assertEquals(coordenadorSalvo.getNome(), returnedCoordenador.getNome());
                Assertions.assertEquals(coordenadorSalvo.getDataCriacao(), returnedCoordenador.getDataCriacao());
                Assertions.assertEquals(coordenadorSalvo.getAtivo(), returnedCoordenador.getAtivo());
        }

        @Test
        @Transactional
        public void deveRemoverCoordenador() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
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
                Coordenador coordenadorSalvo = coordenadorRepositorio.save(coordenador);
                Long id = coordenadorSalvo.getId();
                coordenadorRepositorio.deleteById(id);
                Optional<Coordenador> temp = coordenadorRepositorio.findById(id);

                // rollback
                coordenadorRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertFalse(temp.isPresent());
        }

        @Test
        @Transactional
        public void deveObterCoordenadorPorNome() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
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
                Coordenador coordenadorSalvo = coordenadorRepositorio.save(coordenador);
                Optional<Coordenador> returned = coordenadorRepositorio.findByNome(coordenadorSalvo.getNome());

                // rollback
                coordenadorRepositorio.delete(coordenadorSalvo);
                cursoRepositorio.delete(cursoSalvo);

                // verificação
                Assertions.assertTrue(returned.isPresent());
                Assertions.assertEquals(coordenador.getNome(), returned.get().getNome());
        }
}
