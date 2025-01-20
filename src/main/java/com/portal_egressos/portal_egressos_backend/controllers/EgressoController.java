package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/egresso")
public class EgressoController {

    @Autowired
    private EgressoService egressoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoEgressoService cursoEgressoService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarEgresso(@RequestBody EgressoDTO dto) {
        try {
            Usuario usuario = Usuario.builder()
                    .email(dto.getEmailUsuario())
                    .senha(dto.getSenhaUsuario())
                    .role(UserRole.EGRESSO)
                    .build();

            Egresso egresso = converterParaModelo(dto);
            egresso.setStatus(Status.PENDENTE);
            egresso.setUsuario(usuario);
            Curso curso = cursoService.buscarPorId(dto.getIdCurso());
            Egresso egressoRetornado = egressoService.salvarEgresso(egresso);
            CursoEgresso cursoEgresso = salvarCursoEgresso(dto, egresso, curso);
            cursoEgressoService.salvar(cursoEgresso);

            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(egressoRetornado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarEgresso(@RequestBody EgressoDTO dto, @PathVariable Long id) {
        try {
            Egresso egressoRetornado = egressoService.buscarPorId(id);
            Egresso egresso = converterParaModelo(dto);
            egresso.setId(id);
            egresso.setUsuario(egressoRetornado.getUsuario());
            Egresso egressoAtualizado = egressoService.atualizarEgresso(egresso);
            return ResponseEntity.ok(converterParaDTO(egressoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarEgresso(@PathVariable Long id) {
        try {
            egressoService.removerEgresso(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<?> buscarPorNome(@RequestParam("nome") String nome) {
        try {
            List<Egresso> egressosRetornado = egressoService.buscarEgressoPorNome(nome);
            List<EgressoDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarAprovados")
    public ResponseEntity<?> buscarEgressoAprovados() {
        try {
            List<Egresso> egressosRetornado = egressoService.listarEgressosAprovados();
            List<EgressoDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarEgressos() {
        try {
            List<Egresso> egressosRetornado = egressoService.listarEgressos();
            List<EgressoDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Egresso converterParaModelo(EgressoDTO dto) {
        return Egresso.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .foto(dto.getFoto())
                .linkedin(dto.getLinkedin())
                .instagram(dto.getInstagram())
                .curriculo(dto.getCurriculo())
                .build();
    }

    private CursoEgresso salvarCursoEgresso(EgressoDTO dto, Egresso egressoSalvo, Curso cursoSalvo) {
        return CursoEgresso.builder()
                .egresso(egressoSalvo)
                .curso(cursoSalvo)
                .anoInicio(dto.getAnoInicio())
                .anoFim(dto.getAnoFim())
                .build();
    }

    private EgressoDTO converterParaDTO(Egresso egresso) {
        return EgressoDTO.builder()
                .id(egresso.getId())
                .nome(egresso.getNome())
                .descricao(egresso.getDescricao())
                .foto(egresso.getFoto())
                .linkedin(egresso.getLinkedin())
                .instagram(egresso.getInstagram())
                .curriculo(egresso.getCurriculo())
                .status(egresso.getStatus())
                .idUsuario(egresso.getUsuario().getId())
                .emailUsuario(egresso.getUsuario().getEmail())
                .build();
    }
}
