package com.portal_egressos.portal_egressos_backend.repositories;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorRepositoryTest {

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Test
    public void deveSalvarCoordenador() {
        //cenário
        Usuario usuario = Usuario.builder()
                                 .email("teste@teste.com")
                                 .senha("123456")
                                 .build();

        Curso curso = Curso.builder()
                            .nome("coordenador")
                            .nivel("graduação")
                            .build();

        Curso savedCurso = cursoRepository.save(curso);

        Coordenador coordenador = Coordenador.builder().nome("teste")
                                            .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))  
                                            .ativo(true)      
                                            .curso(savedCurso)
                                            .usuario(usuario)      
                                            .build();
        //ação
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);

        //rollback 
        coordenadorRepository.delete(savedCoordenador);
        cursoRepository.delete(savedCurso);

        //verificação
        Assertions.assertNotNull(savedCoordenador);
        Assertions.assertEquals(coordenador.getNome(), savedCoordenador.getNome());
        Assertions.assertEquals(coordenador.getDataCriacao(), savedCoordenador.getDataCriacao());
        Assertions.assertEquals(coordenador.getAtivo(), savedCoordenador.getAtivo());
    }


    @Test
    public void deveAtualizarCoordenador() {
        //cenário
        Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .build();

        Curso curso = Curso.builder()
                            .nome("coordenador")
                            .nivel("graduação")
                            .build();

        Curso savedCurso = cursoRepository.save(curso);

        Coordenador coordenador = Coordenador.builder().nome("teste")
                                            .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))  
                                            .ativo(true)      
                                            .curso(savedCurso)
                                            .usuario(usuario)      
                                            .build();

        // ação
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);
        savedCoordenador.setNome("teste2@teste.com");
        savedCoordenador.setDataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0));
        savedCoordenador.setAtivo(false);
        Coordenador returnedCoordenador = coordenadorRepository.save(savedCoordenador);

        //rollback 
        coordenadorRepository.delete(savedCoordenador);
        cursoRepository.delete(savedCurso);

        // verificação
        Assertions.assertNotNull(savedCoordenador);
        Assertions.assertEquals(savedCoordenador.getNome(), returnedCoordenador.getNome());
        Assertions.assertEquals(savedCoordenador.getDataCriacao(), returnedCoordenador.getDataCriacao());
        Assertions.assertEquals(savedCoordenador.getAtivo(), returnedCoordenador.getAtivo());
    }


    @Test
    public void deveRemoverCoordenador() {
        //cenário
        Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .build();

        Curso curso = Curso.builder()
                            .nome("coordenador")
                            .nivel("graduação")
                            .build();

        Curso savedCurso = cursoRepository.save(curso);

        Coordenador coordenador = Coordenador.builder().nome("teste")
                                            .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))  
                                            .ativo(true)      
                                            .curso(savedCurso)
                                            .usuario(usuario)      
                                            .build();
        // ação
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);
        Long id = savedCoordenador.getId();
        coordenadorRepository.deleteById(id);
        Optional<Coordenador> temp = coordenadorRepository.findById(id);

        //rollback
        coordenadorRepository.delete(savedCoordenador);
        cursoRepository.delete(savedCurso);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }


    @Test
    public void deveObterCoordenadorPorNome() {
        //cenário
        Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .build();

        Curso curso = Curso.builder()
                            .nome("coordenador")
                            .nivel("graduação")
                            .build();

        Curso savedCurso = cursoRepository.save(curso);

        Coordenador coordenador = Coordenador.builder().nome("teste")
                                            .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))  
                                            .ativo(true)      
                                            .curso(savedCurso)
                                            .usuario(usuario)      
                                            .build();

        // ação
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);
        Optional<Coordenador> returned = coordenadorRepository.findByNome(savedCoordenador.getNome());

        //rollback
        coordenadorRepository.delete(savedCoordenador);
        cursoRepository.delete(savedCurso);

        // verificação
        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(coordenador.getNome(), returned.get().getNome());
    }
}

