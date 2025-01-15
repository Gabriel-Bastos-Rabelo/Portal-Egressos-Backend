package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.dto.CursoDto;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.services.CursoService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;


    @GetMapping("/listarCursos")
    public ResponseEntity buscar() {
        try {
            List<Curso> egressos = cursoService.listarCursos();
            return ResponseEntity.status(HttpStatus.OK).body(egressos);
        } catch (RegraNegocioRunTime e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar_egressos_por_curso/{id}")
    public ResponseEntity<List<CursoDto>> listarEgressosPorCurso(@PathVariable Long id) {
        try {
            Curso curso = cursoService.buscarPorId(id);

            List<Egresso> egressos = cursoService.listarEgressosPorCurso(curso);

            List<CursoDto> cursoDtos = egressos.stream()
                    .map(egresso -> CursoDto.builder()
                            .id(curso.getId())
                            .nome(curso.getNome())
                            .nivel(curso.getNivel())
                            .idEgresso(egresso.getId())
                            .nomeEgresso(egresso.getNome())
                            .build()
                    )
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(cursoDtos);
        } catch (RegraNegocioRunTime e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/listar_quantidade_egressos_por_curso/{id}")
    public ResponseEntity listarQuantidadeDeEgressosPorCurso(@PathVariable Long id) {
        Curso curso = Curso.builder()
                .id(id)
                .build();
        try {
            int quantidade = cursoService.listarQuantidadeDeEgressosPorCurso(curso);
            return ResponseEntity.status(HttpStatus.OK).body(quantidade);
        } catch (RegraNegocioRunTime e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}

