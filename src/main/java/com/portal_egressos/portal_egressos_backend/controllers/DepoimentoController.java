package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.dto.DepoimentoDto;
import com.portal_egressos.portal_egressos_backend.dto.DepoimentoResponseDTO;
import com.portal_egressos.portal_egressos_backend.dto.OportunidadeDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.DepoimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/depoimento")
public class DepoimentoController {

    @Autowired
    private DepoimentoService depoimentoService;

    @Autowired
    private EgressoService egressoService;

    @Autowired
    private CursoEgressoService cursoEgressoService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarDepoimento(@RequestBody DepoimentoDto depoimentoDTO) {
        try {
            Egresso egresso = egressoService.buscarPorId(depoimentoDTO.getIdEgresso());
            Depoimento depoimento = converterParaModelo(depoimentoDTO);
            depoimento.setEgresso(egresso);
            Depoimento depoimentoSalva = depoimentoService.salvarDepoimento(depoimento);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(depoimentoSalva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizarDepoimento(@PathVariable Long id, @RequestBody DepoimentoDto depoimentoDTO) {
        try {
            Depoimento depoimento = converterParaModelo(depoimentoDTO);
            depoimento.setId(id);

            if (depoimentoDTO.getIdEgresso() != null) {
                Egresso egresso = egressoService.buscarPorId(depoimentoDTO.getIdEgresso());
                depoimento.setEgresso(egresso);
            }

            Depoimento depoimentoAtualizada = depoimentoService.atualizarDepoimento(depoimento);
            return ResponseEntity.ok(converterParaDTO(depoimentoAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("status/{id}")
    public ResponseEntity<?> atualizarStatusDepoimento(@PathVariable("id") Long id) {
        try {
            Status status = Status.APROVADO;

            Depoimento depoimento = Depoimento.builder()
                    .id(id)
                    .status(status)
                    .build();
            Depoimento depoimentoAtualizada = depoimentoService.atualizarStatusDepoimento(depoimento);
            return ResponseEntity.ok(converterParaDTO(depoimentoAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/aprovar")
    public ResponseEntity<?> aprovarDepoimento(@RequestBody List<Long> ids) {

        try {
            depoimentoService.aprovarDepoimentos(ids);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reprovar")
    public ResponseEntity<?> reprovarDepoimento(@RequestBody List<Long> ids) {

        try {
            depoimentoService.reprovarDepoimentos(ids);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarDepoimentos() {
        try {
            List<Depoimento> depoimentos = depoimentoService.listarDepoimentos();
            List<DepoimentoResponseDTO> depoimentosDTO = depoimentos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(depoimentosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/aprovados")
    public ResponseEntity<?> listarDepoimentosAprovadas() {
        try {
            List<Depoimento> depoimentos = depoimentoService.listarDepoimentosAprovados();
            List<DepoimentoResponseDTO> depoimentosDTO = depoimentos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(depoimentosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendentes")
    public ResponseEntity<?> listarPendentes() {
        try {
            List<Depoimento> depoimentos = depoimentoService.listarDepoimentosPendentes();
            List<DepoimentoResponseDTO> depoimentoDTO = depoimentos.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(depoimentoDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("remover/{id}")
    public ResponseEntity<?> removerDepoimento(@PathVariable Long id) {
        try {
            depoimentoService.removerDepoimento(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarDepoimentoPorEgresso(@PathVariable Long id) {
        Optional<Depoimento> depoimentoOpt = depoimentoService.buscarDepoimentoPorEgresso(id);

        if (depoimentoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Depoimento não encontrado para o egresso com id " + id);
        }

        DepoimentoResponseDTO dto = converterParaDTO(depoimentoOpt.get());

        return ResponseEntity.ok(dto);
    }

    private Depoimento converterParaModelo(DepoimentoDto dto) {
        return Depoimento.builder()
                .id(dto.getId())
                .texto(dto.getTexto())
                .data(dto.getData())
                .status(Status.PENDENTE)
                .build();
    }

    private DepoimentoResponseDTO converterParaDTO(Depoimento depoimento) {
        Optional<CursoEgresso> cursoEgresso = cursoEgressoService.buscarPorId(depoimento.getEgresso().getId());
        Curso curso = cursoEgresso.isPresent() ? cursoEgresso.get().getCurso() : null;

        return DepoimentoResponseDTO.builder()
                .id(depoimento.getId())
                .descricao(depoimento.getTexto())
                .data(depoimento.getData())
                .status(depoimento.getStatus())
                .idEgresso(depoimento.getEgresso().getId())
                .nomeEgresso(depoimento.getEgresso().getNome())
                .nomeCurso(curso != null ? curso.getNome() : null)
                .build();
    }
}
