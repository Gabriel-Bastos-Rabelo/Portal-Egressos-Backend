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
import com.portal_egressos.portal_egressos_backend.repositories.CursoEgressoRepository;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.enums.Status;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CursoEgressoTest {

    @Autowired
    CursoService cursoService;

    @Autowired
    EgressoService egressoService;

    @Autowired
    CursoEgressoService cursoEgressoService;

    @Autowired
    CursoEgressoRepository cursoEgressoRepo;

    @Test
    @Transactional
    public void deveSalvarCursoEgresso() {
        // Cenário: Criar cursos, egressos e associá-los
        List<Curso> cursos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cursos.add(Curso.builder()
                    .nome("Curso teste" + (i + 1))
                    .nivel("Nível teste" + (i + 1))
                    .build());
        }

        List<Curso> retornoCurso = new ArrayList<>();
        for (Curso curso : cursos) {
            retornoCurso.add(cursoService.salvarCurso(curso));
        }

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(Usuario.builder()
                    .email("teste" + i + "@teste.com")
                    .senha("senha123" + i)
                    .role(UserRole.EGRESSO)
                    .build());
        }

        List<Egresso> egressos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            egressos.add(Egresso.builder()
                    .nome("Egresso Teste" + (i + 1))
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

        // Ação
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
        for (CursoEgresso cursoEgresso : cursoEgressos) {
            Assertions.assertThat(cursoEgresso.getId()).isNotNull();
            Assertions.assertThat(cursoEgresso.getAnoInicio()).isEqualTo(2020);
            Assertions.assertThat(cursoEgresso.getAnoFim()).isEqualTo(2023);
        }
    }

    @Test
    @Transactional
    public void deveAtualizarCursoEgresso() {

        // cenário
        List<Curso> cursos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cursos.add(
                    Curso.builder()
                            .nome("Curso teste" + (i + 1))
                            .nivel("Nível teste" + (i + 1))
                            .build());
        }

        List<Curso> retornoCurso = new ArrayList<>();
        for (Curso curso : cursos) {
            retornoCurso.add(cursoService.salvarCurso(curso));
        }

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(
                    Usuario.builder().email("teste" + i + "@teste.com")
                            .senha("senha123" + i)
                            .role(UserRole.EGRESSO)
                            .build());
        }

        List<Egresso> egressos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            egressos.add(
                    Egresso.builder().nome("Egresso Teste" + (i + 1))
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

        // Ação
        for (CursoEgresso cursoEgresso : cursoEgressos) {
            cursoEgresso.setAnoInicio(2021);
            cursoEgresso.setAnoFim(2024);
            cursoEgressoService.atualizar(cursoEgresso);
        }

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
        for (CursoEgresso cursoEgresso : cursoEgressos) {
            Assertions.assertThat(cursoEgresso.getAnoInicio()).isEqualTo(2021);
            Assertions.assertThat(cursoEgresso.getAnoFim()).isEqualTo(2024);
        }

    }

    @Test
    @Transactional
    public void deveRemoverCursoEgresso() {
        // Cenário
        List<Curso> cursos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            cursos.add(Curso.builder()
                    .nome("Curso teste" + (i + 1))
                    .nivel("Nível teste" + (i + 1))
                    .build());
        }

        List<Curso> retornoCurso = new ArrayList<>();
        for (Curso curso : cursos) {
            retornoCurso.add(cursoService.salvarCurso(curso));
        }

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(Usuario.builder()
                    .email("teste" + i + "@teste.com")
                    .senha("senha123" + i)
                    .role(UserRole.EGRESSO)
                    .build());
        }

        List<Egresso> egressos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            egressos.add(Egresso.builder()
                    .nome("Egresso Teste" + (i + 1))
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
        CursoEgresso cursoEgressoARemover = cursoEgressos.get(0);
        cursoEgressoService.remover(cursoEgressoARemover);

        // Verificação
        List<CursoEgresso> cursoEgressoRemovido = cursoEgressoRepo.findAll();
        Assertions.assertThat(cursoEgressoRemovido).doesNotContain(cursoEgressoARemover);

    }

}
