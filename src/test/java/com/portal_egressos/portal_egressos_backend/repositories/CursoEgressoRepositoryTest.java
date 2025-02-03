package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CursoEgressoRepositoryTest {

        @Autowired
        CursoEgressoRepository cursoEgressoRepositorio;

        @Autowired
        EgressoRepository egressoRepositorio;

        @Autowired
        CursoRepository cursoRepositorio;

        @Test
        @Transactional
        public void deveSalvarCursoDeEgresso() {
                // cenário
                Curso curso = Curso.builder()
                                .nome("curso teste")
                                .nivel("nivel teste")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("teste_linkedin")
                .instagram("teste_instagram")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .egresso(egressoSalvo)
                                .curso(cursoSalvo)
                                .anoInicio(2015)
                                .anoFim(2019)
                                .build();

                // ação
                CursoEgresso cursoEgressoSalvo = cursoEgressoRepositorio.save(cursoEgresso);

                // rollback
                cursoEgressoRepositorio.delete(cursoEgressoSalvo);
                cursoRepositorio.delete(cursoSalvo);
                egressoRepositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertNotNull(cursoEgressoSalvo);
                Assertions.assertNotNull(cursoEgressoSalvo.getId());
                Assertions.assertEquals(cursoEgresso.getEgresso(), cursoEgressoSalvo.getEgresso());
                Assertions.assertEquals(cursoEgresso.getCurso(), cursoEgressoSalvo.getCurso());
                Assertions.assertEquals(cursoEgresso.getAnoInicio(), cursoEgressoSalvo.getAnoInicio());
                Assertions.assertEquals(cursoEgresso.getAnoFim(), cursoEgressoSalvo.getAnoFim());
        }

        @Test
        @Transactional
        public void deveAtualizarCursoEgresso() {
                // cenário
                Curso curso = Curso.builder()
                                .nome("curso teste")
                                .nivel("nivel teste")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("teste_linkedin")
                .instagram("teste_instagram")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .egresso(egressoSalvo)
                                .curso(cursoSalvo)
                                .anoInicio(2015)
                                .anoFim(2019)
                                .build();

                // ação
                CursoEgresso cursoEgressoSalvo = cursoEgressoRepositorio.save(cursoEgresso);
                cursoEgressoSalvo.setAnoInicio(2010);
                cursoEgressoSalvo.setAnoFim(2024);
                CursoEgresso cursoEgressoAtualizado = cursoEgressoRepositorio.save(cursoEgressoSalvo);

                // rollback
                cursoEgressoRepositorio.delete(cursoEgressoSalvo);
                cursoRepositorio.delete(cursoSalvo);
                egressoRepositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertNotNull(cursoEgressoAtualizado);
                Assertions.assertEquals(2010, cursoEgressoAtualizado.getAnoInicio());
                Assertions.assertEquals(2024, cursoEgressoAtualizado.getAnoFim());

        }

        @Test
        @Transactional
        public void deveRemoverCursoEgresso() {
                // cenário
                Curso curso = Curso.builder()
                                .nome("curso teste")
                                .nivel("nivel teste")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("teste_linkedin")
                .instagram("teste_instagram")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .egresso(egressoSalvo)
                                .curso(cursoSalvo)
                                .anoInicio(2015)
                                .anoFim(2019)
                                .build();

                // ação
                CursoEgresso cursoEgressoSalvo = cursoEgressoRepositorio.save(cursoEgresso);
                cursoEgressoRepositorio.deleteById(cursoEgressoSalvo.getId());
                Optional<CursoEgresso> excluido = cursoEgressoRepositorio.findById(cursoEgressoSalvo.getId());

                // Rollbacks
                cursoEgressoRepositorio.delete(cursoEgressoSalvo);
                cursoRepositorio.delete(cursoSalvo);
                egressoRepositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertFalse(excluido.isPresent());

        }

        @Test
        @Transactional
        public void deveObterCursoEgressoPorId() {
                // cenário
                Curso curso = Curso.builder()
                                .nome("curso teste")
                                .nivel("nivel teste")
                                .build();

                Curso cursoSalvo = cursoRepositorio.save(curso);

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
                                .build();

        Egresso egresso = Egresso.builder().nome("teste")
                .descricao("lorem ipsum lore")
                .foto("urlteste")
                .linkedin("teste_linkedin")
                .instagram("teste_instagram")
                .curriculo("lorem ipsum lore")
                .usuario(usuario)
                                .status(Status.PENDENTE)
                .build();

                Egresso egressoSalvo = egressoRepositorio.save(egresso);

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .egresso(egressoSalvo)
                                .curso(cursoSalvo)
                                .anoInicio(2015)
                                .anoFim(2019)
                                .build();

                CursoEgresso cursoEgressoSalvo = cursoEgressoRepositorio.save(cursoEgresso);

                // ação
                Optional<CursoEgresso> cursoEgressoRetornado = cursoEgressoRepositorio
                                .findById(cursoEgressoSalvo.getId());

                // rollback
                cursoEgressoRepositorio.delete(cursoEgressoSalvo);
                cursoRepositorio.delete(cursoSalvo);
                egressoRepositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertTrue(cursoEgressoRetornado.isPresent());
                Assertions.assertEquals(cursoEgressoSalvo.getId(), cursoEgressoRetornado.get().getId());
                Assertions.assertEquals(cursoEgressoSalvo.getEgresso().getNome(),
                                cursoEgressoRetornado.get().getEgresso().getNome());
                Assertions.assertEquals(cursoEgressoSalvo.getCurso().getNome(),
                                cursoEgressoRetornado.get().getCurso().getNome());
                Assertions.assertEquals(cursoEgressoSalvo.getAnoInicio(), cursoEgressoRetornado.get().getAnoInicio());
                Assertions.assertEquals(cursoEgressoSalvo.getAnoFim(), cursoEgressoRetornado.get().getAnoFim());

        }

}
