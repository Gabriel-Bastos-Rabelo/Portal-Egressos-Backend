package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Curso;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CursoRepositoryTest {

    @Autowired
    CursoRepository cursoRepositorio;

    @Test
    @Transactional
    public void deveVerificarSalvarCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nivel Teste")
                .build();

        // ação
        Curso cursoSalvo = cursoRepositorio.save(curso);

        // rollback
        cursoRepositorio.delete(cursoSalvo);

        // verificação
        Assertions.assertNotNull(cursoSalvo);
        Assertions.assertEquals(curso.getNome(), cursoSalvo.getNome());
        Assertions.assertEquals(curso.getNivel(), cursoSalvo.getNivel());

    }

    @Test
    @Transactional
    public void deveAtualizarCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nivel Teste")
                .build();

        // ação
        Curso cursoSalvo = cursoRepositorio.save(curso);

        cursoSalvo.setNome("Nome atualizado");
        cursoSalvo.setNivel("Nivel atualizado");

        Curso cursoRetornado = cursoRepositorio.save(cursoSalvo);

        // rollback
        cursoRepositorio.delete(cursoSalvo);
        cursoRepositorio.delete(cursoRetornado);

        // verificação
        Assertions.assertNotNull(cursoRetornado);
        Assertions.assertEquals(cursoSalvo.getNome(), cursoRetornado.getNome());
        Assertions.assertEquals(cursoSalvo.getNivel(), cursoRetornado.getNivel());
    }

    @Test
    @Transactional
    public void deveRemoverCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        // ação
        Curso cursoSalvo = cursoRepositorio.save(curso);
        Long id = cursoSalvo.getId();
        cursoRepositorio.deleteById(id);
        Optional<Curso> temp = cursoRepositorio.findById(id);

        // rollback
        cursoRepositorio.delete(cursoSalvo);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    @Transactional
    public void deveObterCursoPorNivel() {
        // cenário
        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso cursoSalvo = cursoRepositorio.save(curso);

        // ação
        Optional<Curso> cursoRetornado = cursoRepositorio.findByNivel(curso.getNivel());

        // rollback
        cursoRepositorio.delete(cursoSalvo);

        // verificação
        Assertions.assertTrue(cursoRetornado.isPresent());
        Assertions.assertEquals(curso.getNivel(), cursoRetornado.get().getNivel());

    }

}
