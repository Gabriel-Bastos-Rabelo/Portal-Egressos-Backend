package com.portal_egressos.portal_egressos_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.enums.Status;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CursoServiceTest<cursoEgressoService> {

    @Autowired
    CursoService cursoService;

    @Autowired
    EgressoService egressoService;

    @Autowired
    CursoEgressoService cursoEgressoService;

    @Test
    @Transactional
    public void deveSalvarCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nível Teste")
                .build();

        // ação
        Curso retorno = cursoService.salvarCurso(curso);

        // rollback
        cursoService.removerCurso(retorno);

        // verificação
        Assertions.assertThat(retorno).isNotNull();
        Assertions.assertThat(retorno.getNome()).isEqualTo(curso.getNome());
        Assertions.assertThat(retorno.getNivel()).isEqualTo(curso.getNivel());

    }

    @Test
    @Transactional
    public void deveAtualizarCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nível Teste")
                .build();

        Curso retorno = cursoService.salvarCurso(curso);

        // ação
        retorno.setNome("Teste curso");
        retorno.setNivel("Teste nivel");

        Curso salvo = cursoService.salvarCurso(retorno);

        // rollback
        cursoService.removerCurso(retorno);

        // Verificação
        Assertions.assertThat(salvo).isNotNull();
        Assertions.assertThat(salvo.getNome()).isEqualTo(salvo.getNome());
        Assertions.assertThat(salvo.getNivel()).isEqualTo(salvo.getNivel());

    }

    @Test
    @Transactional
    public void deveRemoverCurso() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nível Teste")
                .build();

        Curso retorno = cursoService.salvarCurso(curso);

        // ação
        cursoService.removerCurso(retorno);
        List<Curso> temp = cursoService.listarCursos();

        // verificação
        Assertions.assertThat(temp.isEmpty());

    }

    @Test
    @Transactional
    public void deveListarCursos() {
        // cenário
        Curso curso1 = Curso.builder()
                .nome("Curso 1")
                .nivel("Nível 1")
                .build();
        Curso curso2 = Curso.builder()
                .nome("Curso 2")
                .nivel("Nível 2")
                .build();

        cursoService.salvarCurso(curso1);
        cursoService.salvarCurso(curso2);

        // ação
        List<Curso> cursos = cursoService.listarCursos();

        // rollback
        cursoService.removerCurso(curso1);
        cursoService.removerCurso(curso2);

        // verificação
        Assertions.assertThat(cursos).isNotEmpty();
        Assertions.assertThat(cursos).contains(curso1, curso2);

    }

    @Test
    @Transactional
    public void deveBuscarPorId() {
        // cenário
        Curso curso = Curso.builder()
                .nome("Curso Teste")
                .nivel("Nível Teste")
                .build();

        Curso cursoSalvo = cursoService.salvarCurso(curso);

        // ação
        Curso cursoBuscado = cursoService.buscarPorId(cursoSalvo.getId());

        // rollback
        cursoService.removerCurso(cursoSalvo);

        // verificação
        Assertions.assertThat(cursoBuscado.getId()).isEqualTo(cursoSalvo.getId());
        Assertions.assertThat(cursoBuscado.getNome()).isEqualTo("Curso Teste");
        Assertions.assertThat(cursoBuscado.getNivel()).isEqualTo("Nível Teste");

    }

    @Test
    @Transactional
    public void deveListarEgressosPorCurso() {
        // cenário
        List<Curso> cursos = new ArrayList<Curso>();
        for (int i = 0; i < 2; i++) {
            cursos.add(
                    Curso.builder()
                            .nome("Curso teste" + (i + 1))
                            .nivel("Nível teste" + (i + 1))
                            .build());
        }

        List<Curso> retornoCurso = new ArrayList<Curso>();

        for (Curso curso : cursos) {
            retornoCurso.add(cursoService.salvarCurso(curso));
        }

        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(
                    Usuario.builder().email("teste" + i + "@teste.com")
                            .senha("senha123" + i)
                            .role(UserRole.EGRESSO)
                            .build());
        }

        List<Egresso> egressos = new ArrayList<Egresso>();
        for (int i = 0; i < 2; i++) {
            egressos.add(
                    Egresso.builder().nome("teste")
                            .descricao("lorem ipsum lore")
                            .foto("urlteste")
                            .linkedin("https://www.linkedin.com/in/usuario" + (i + 1))
                            .instagram("https://www.instagram.com/usuario" + (i + 1))
                            .curriculo("lorem ipsum lore")
                            .usuario(usuarios.get(i))
                            .status(Status.PENDENTE)
                            .build());
        }

        List<Egresso> retornoEgresso = new ArrayList<>();

        for (Egresso egresso : egressos) {
            retornoEgresso.add(egressoService.salvarEgresso(egresso));
        }

        List<CursoEgresso> cursoEgressos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            CursoEgresso cursoEgresso = CursoEgresso.builder()
                    .egresso(retornoEgresso.get(i))
                    .curso(retornoCurso.get(i))
                    .anoInicio(2020)
                    .anoFim(2023)
                    .build();

            cursoEgressos.add(cursoEgressoService.salvar(cursoEgresso));
        }

        // ação
        List<Egresso> retorno = cursoService.listarEgressosPorCurso(cursos.get(0));

        // Rollback
        for (CursoEgresso cursoEgresso : cursoEgressos) {
            cursoEgressoService.remover(cursoEgresso);
        }
        for (Curso curso : retornoCurso) {
            cursoService.removerCurso(curso);
        }
        for (Egresso egresso : retornoEgresso) {
            egressoService.removerEgresso(egresso.getId());
        }

        // Verificação
        Assertions.assertThat(retorno).isNotNull();
        Assertions.assertThat(retorno.size()).isEqualTo(1); // Espera-se 1 egresso para o curso
        for (Egresso egresso : retorno) {
            Assertions.assertThat(egresso.getId()).isEqualTo(cursoEgressos.get(0).getEgresso().getId());
            Assertions.assertThat(egresso.getNome()).isEqualTo(cursoEgressos.get(0).getEgresso().getNome());
        }

    }

    @Test
    @Transactional
    public void deveListarQuantidadeEgressosPorCurso() {
        // cenário
        List<Curso> cursos = new ArrayList<Curso>();
        for (int i = 0; i < 2; i++) {
            cursos.add(
                    Curso.builder()
                            .nome("Curso teste" + (i + 1))
                            .nivel("Nível teste" + (i + 1))
                            .build());
        }

        List<Curso> retornoCurso = new ArrayList<Curso>();

        for (Curso curso : cursos) {
            retornoCurso.add(cursoService.salvarCurso(curso));
        }

        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(
                    Usuario.builder().email("teste" + i + "@teste.com")
                            .senha("senha123" + i)
                            .role(UserRole.EGRESSO)
                            .build());
        }

        List<Egresso> egressos = new ArrayList<Egresso>();
        for (int i = 0; i < 2; i++) {
            egressos.add(
                    Egresso.builder().nome("teste")
                            .descricao("lorem ipsum lore")
                            .foto("urlteste")
                            .linkedin("https://www.linkedin.com/in/usuario" + (i + 1)) // URL válida
                            .instagram("https://www.instagram.com/usuario" + (i + 1)) // URL válida
                            .curriculo("lorem ipsum lore")
                            .usuario(usuarios.get(i))
                            .status(Status.PENDENTE)
                            .build());
        }

        List<Egresso> retornoEgresso = new ArrayList<>();

        for (Egresso egresso : egressos) {
            retornoEgresso.add(egressoService.salvarEgresso(egresso));
        }

        List<CursoEgresso> cursoEgressos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            CursoEgresso cursoEgresso = CursoEgresso.builder()
                    .egresso(retornoEgresso.get(i))
                    .curso(retornoCurso.get(i))
                    .anoInicio(2020)
                    .anoFim(2023)
                    .build();

            cursoEgressos.add(cursoEgressoService.salvar(cursoEgresso));
        }

        // Ação
        int quantidadeEgressosCursoId1 = cursoService.listarQuantidadeDeEgressosPorCurso(cursos.get(0));
        int quantidadeEgressosCursoId2 = cursoService.listarQuantidadeDeEgressosPorCurso(cursos.get(1));

        // Rollback
        for (CursoEgresso cursoEgresso : cursoEgressos) {
            cursoEgressoService.remover(cursoEgresso);
        }
        for (Curso curso : retornoCurso) {
            cursoService.removerCurso(curso);
        }
        for (Egresso egresso : retornoEgresso) {
            egressoService.removerEgresso(egresso.getId());
        }

        // Verificação
        Assertions.assertThat(quantidadeEgressosCursoId1).isNotNull();
        Assertions.assertThat(quantidadeEgressosCursoId1).isEqualTo(1);

        Assertions.assertThat(quantidadeEgressosCursoId2).isNotNull();
        Assertions.assertThat(quantidadeEgressosCursoId2).isEqualTo(1);
    }
}
