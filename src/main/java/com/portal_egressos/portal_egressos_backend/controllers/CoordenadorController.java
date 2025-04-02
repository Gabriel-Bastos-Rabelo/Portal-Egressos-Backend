package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.CoordenadorDTO;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.repositories.CursoRepository;
import com.portal_egressos.portal_egressos_backend.services.CoordenadorService;

@RestController
@RequestMapping("/api/coordenador")
public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    @Autowired
    private CursoRepository cursoRepository;

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarCoordenador(@RequestBody CoordenadorDTO dto, @PathVariable Long id) {
        try {
            Coordenador coordenadorRetornado = coordenadorService.buscarPorId(id);
            Coordenador coordenador = converterParaModelo(dto);
            coordenador.setId(coordenadorRetornado.getId());
            coordenador.setUsuario(coordenadorRetornado.getUsuario());
            Coordenador coordenadorAtualizado = coordenadorService.atualizarCoordenador(coordenador);
            return ResponseEntity.ok(converterParaDTO(coordenadorAtualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarCoordenadores() {
        try {
            List<Coordenador> coordenadores = coordenadorService.listarCoordenadores();
            List<CoordenadorDTO> coordenadoresDTO = coordenadores.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(coordenadoresDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<?> buscarCoordenadorPorNome(@RequestParam("nome") String nome) {
        try {
            List<Coordenador> coordenadorRetornado = coordenadorService.buscarCoordenadorPorNome(nome);
            List<CoordenadorDTO> coordenadorDTO = coordenadorRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(coordenadorDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarCoordenadorPorId(@PathVariable Long id) {
        try {
            Coordenador coordenador = coordenadorService.buscarPorId(id);
            CoordenadorDTO dto = converterParaDTO(coordenador);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Coordenador converterParaModelo(CoordenadorDTO dto) {
        //Pra poder editar o curso 
        Curso curso = cursoRepository.findById(dto.getIdCurso())
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        return Coordenador.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .dataCriacao(dto.getDataCriacao())
                .ativo(dto.getAtivo())
                .curso(curso)  
                .build();
    }


    private CoordenadorDTO converterParaDTO(Coordenador coordenador) {
        return CoordenadorDTO.builder()
                .id(coordenador.getId())
                .nome(coordenador.getNome())
                .dataCriacao(coordenador.getDataCriacao())
                .ativo(coordenador.getAtivo())
                .idCurso(coordenador.getCurso().getId())
                .nomeCurso(coordenador.getCurso().getNome())
                .idUsuario(coordenador.getUsuario().getId())
                .emailUsuario(coordenador.getUsuario().getEmail())
                .build();
    }
}
