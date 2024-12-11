package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Curso;



@SpringBootTest
@ActiveProfiles("test")
public class CursoRepositoryTest {

    @Autowired
    CursoRepository cursoRepository;

    @Test
    public void deveVerificarSalvarCurso() {
        //cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nivel Teste")
                .build();

        //ação
        Curso cursoSalvo = cursoRepository.save(curso);

        //rollback
        cursoRepository.delete(cursoSalvo);
        
        // verificação
        Assertions.assertNotNull(cursoSalvo);
        Assertions.assertEquals(curso.getNome(), cursoSalvo.getNome());
        Assertions.assertEquals(curso.getNivel(), cursoSalvo.getNivel());


    }

    @Test
    public void deveAtualizarCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nivel Teste")
                .build();

        // ação
        Curso saved = cursoRepository.save(curso);

        saved.setNome("Nome atualizado");
        saved.setNivel("Nivel atualizado");

        Curso returned = cursoRepository.save(saved);

        //rollback
        cursoRepository.delete(saved);

        // verificação
        Assertions.assertNotNull(returned);
        Assertions.assertEquals(saved.getNome(), returned.getNome());
        Assertions.assertEquals(saved.getNivel(), returned.getNivel());
    }

    @Test
    public void deveRemoverCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        // ação
        Curso saved = cursoRepository.save(curso);
        Long id = saved.getId();
        cursoRepository.deleteById(id);
        Optional<Curso> temp = cursoRepository.findById(id);

        //rollback
        cursoRepository.delete(saved);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    public void deveObterCursoPorNivel() {
        // cenário
        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso saved = cursoRepository.save(curso);

        // ação
        Optional<Curso> returned = cursoRepository.findByNivel(curso.getNivel());

        // rollback
        cursoRepository.delete(saved);

        // verificação
        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(curso.getNivel(), returned.get().getNivel());

    }


}
