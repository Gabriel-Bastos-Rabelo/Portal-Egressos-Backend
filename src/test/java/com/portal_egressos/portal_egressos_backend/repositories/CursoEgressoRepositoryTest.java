package com.portal_egressos.portal_egressos_backend.repositories;


import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;


@SpringBootTest
@ActiveProfiles("test")
public class CursoEgressoRepositoryTest {

    @Autowired
    CursoEgressoRepository cursoEgressoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Test
    public void deveSalvarCursoDeEgresso() {
        // cenário


        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso retornoCurso = cursoRepository.save(curso);

        Usuario usuario = Usuario.builder()
            .email("teste@teste.com")
            .senha("123456")
            .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                        .descricao("lorem ipsum lore")  
                                        .foto("urlteste")      
                                        .linkedin("teste_linkedin")      
                                        .instagram("teste_instagram")      
                                        .curriculo("lorem ipsum lore")
                                        .usuario(usuario) 
                                        .build();

        Egresso retornoEgresso = egressoRepository.save(egresso);

        CursoEgresso cursoEgresso = CursoEgresso.builder()
            .egresso(retornoEgresso)
            .curso(retornoCurso)
            .anoInicio(2015)
            .anoFim(2019)
            .build();

        // ação
        CursoEgresso retornoCursoEgresso = cursoEgressoRepository.save(cursoEgresso);
        
        // rollback
        cursoEgressoRepository.delete(retornoCursoEgresso);
        cursoRepository.delete(retornoCurso);
        egressoRepository.delete(retornoEgresso);

        // verificação
        Assertions.assertNotNull(retornoCursoEgresso);
        Assertions.assertNotNull(retornoCursoEgresso.getId());
        Assertions.assertEquals(cursoEgresso.getEgresso(), retornoCursoEgresso.getEgresso());
        Assertions.assertEquals(cursoEgresso.getCurso(), retornoCursoEgresso.getCurso());
        Assertions.assertEquals(cursoEgresso.getAnoInicio(), retornoCursoEgresso.getAnoInicio());
        Assertions.assertEquals(cursoEgresso.getAnoFim(), retornoCursoEgresso.getAnoFim());
    }
    
    @Test
    public void deveAtualizarCursoEgresso() {
        // cenário

        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso retornoCurso = cursoRepository.save(curso);

        Usuario usuario = Usuario.builder()
            .email("teste@teste.com")
            .senha("123456")
            .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                        .descricao("lorem ipsum lore")  
                                        .foto("urlteste")      
                                        .linkedin("teste_linkedin")      
                                        .instagram("teste_instagram")      
                                        .curriculo("lorem ipsum lore")
                                        .usuario(usuario) 
                                        .build();

        Egresso retornoEgresso = egressoRepository.save(egresso);

        CursoEgresso cursoEgresso = CursoEgresso.builder()
            .egresso(retornoEgresso)
            .curso(retornoCurso)
            .anoInicio(2015)
            .anoFim(2019)
            .build();

        // ação
        CursoEgresso retornoCursoEgresso = cursoEgressoRepository.save(cursoEgresso);
        retornoCursoEgresso.setAnoInicio(2010);
        retornoCursoEgresso.setAnoFim(2024);
        CursoEgresso cursoEgressoAtualizado = cursoEgressoRepository.save(retornoCursoEgresso);

        // rollback
        cursoEgressoRepository.delete(retornoCursoEgresso);
        cursoRepository.delete(retornoCurso);
        egressoRepository.delete(retornoEgresso);

       // verificação
       Assertions.assertNotNull(cursoEgressoAtualizado);
       Assertions.assertEquals(2010, cursoEgressoAtualizado.getAnoInicio());
       Assertions.assertEquals(2024, cursoEgressoAtualizado.getAnoFim());


 
    }

    
    @Test
    public void deveRemoverCursoEgresso() {
        // cenário

        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso retornoCurso = cursoRepository.save(curso);

        Usuario usuario = Usuario.builder()
            .email("teste@teste.com")
            .senha("123456")
            .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                        .descricao("lorem ipsum lore")  
                                        .foto("urlteste")      
                                        .linkedin("teste_linkedin")      
                                        .instagram("teste_instagram")      
                                        .curriculo("lorem ipsum lore")
                                        .usuario(usuario) 
                                        .build();

        Egresso retornoEgresso = egressoRepository.save(egresso);

        CursoEgresso cursoEgresso = CursoEgresso.builder()
            .egresso(retornoEgresso)
            .curso(retornoCurso)
            .anoInicio(2015)
            .anoFim(2019)
            .build();

        // ação
        CursoEgresso retornoCursoEgresso = cursoEgressoRepository.save(cursoEgresso);
        cursoEgressoRepository.deleteById(retornoCursoEgresso.getId());
        Optional<CursoEgresso> excluido = cursoEgressoRepository.findById(retornoCursoEgresso.getId());

       //Rollbacks
       cursoEgressoRepository.delete(retornoCursoEgresso);
       cursoRepository.delete(retornoCurso);
       egressoRepository.delete(retornoEgresso);

       // verificação
       Assertions.assertFalse(excluido.isPresent());

    }

    
    @Test
    public void deveObterCursoEgressoPorId() {
        // cenário

        Curso curso = Curso.builder()
                .nome("curso teste")
                .nivel("nivel teste")
                .build();

        Curso retornoCurso = cursoRepository.save(curso);

        Usuario usuario = Usuario.builder()
            .email("teste@teste.com")
            .senha("123456")
            .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                        .descricao("lorem ipsum lore")  
                                        .foto("urlteste")      
                                        .linkedin("teste_linkedin")      
                                        .instagram("teste_instagram")      
                                        .curriculo("lorem ipsum lore")
                                        .usuario(usuario) 
                                        .build();

        Egresso retornoEgresso = egressoRepository.save(egresso);

        CursoEgresso cursoEgresso = CursoEgresso.builder()
            .egresso(retornoEgresso)
            .curso(retornoCurso)
            .anoInicio(2015)
            .anoFim(2019)
            .build();

        
        CursoEgresso retornoCursoEgresso = cursoEgressoRepository.save(cursoEgresso);

        // ação
        Optional<CursoEgresso> retornoCursoEgressoEncontrado = cursoEgressoRepository.findById(retornoCursoEgresso.getId());

         // rollback
         cursoEgressoRepository.delete(retornoCursoEgresso);
         cursoRepository.delete(retornoCurso);
         egressoRepository.delete(retornoEgresso);


       // verificação
        Assertions.assertTrue(retornoCursoEgressoEncontrado.isPresent());
        Assertions.assertEquals(retornoCursoEgresso.getId(), retornoCursoEgressoEncontrado.get().getId());
        Assertions.assertEquals(retornoCursoEgresso.getEgresso().getNome(), retornoCursoEgressoEncontrado.get().getEgresso().getNome());
        Assertions.assertEquals(retornoCursoEgresso.getCurso().getNome(), retornoCursoEgressoEncontrado.get().getCurso().getNome());
        Assertions.assertEquals(retornoCursoEgresso.getAnoInicio(), retornoCursoEgressoEncontrado.get().getAnoInicio());
        Assertions.assertEquals(retornoCursoEgresso.getAnoFim(), retornoCursoEgressoEncontrado.get().getAnoFim());

    }
 
}
