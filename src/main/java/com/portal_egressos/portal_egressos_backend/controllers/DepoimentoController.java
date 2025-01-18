package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.dto.DepoimentoDto;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.DepoimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/depoimento")
public class DepoimentoController {

    @Autowired
    private DepoimentoService depoimentoService;

    @Autowired
    private EgressoService egressoService;

    
    @PostMapping("/salvar")
    public ResponseEntity<?> salvarDepoimento(@RequestBody DepoimentoDto depoimentoDTO) {
        try {
            Egresso filtro = Egresso.builder().id(depoimentoDTO.getIdEgresso()).build();

            List<Egresso> egressos = egressoService.buscarEgresso(filtro);
            if (egressos.isEmpty()) {
                throw new RegraNegocioRunTime("Egresso não encontrado para o ID: " + depoimentoDTO.getIdEgresso());
            }

            Egresso egresso = egressos.get(0);
            Depoimento depoimento = converterParaModelo(depoimentoDTO);
            depoimento.setEgresso(egresso);

            Depoimento depoimentoSalva = depoimentoService.salvarDepoimento(depoimento);
            return ResponseEntity.ok(converterParaDTO(depoimentoSalva));
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizarDepoimento(@PathVariable Long id, @RequestBody DepoimentoDto depoimentoDTO) {
        try {
            Depoimento depoimento = converterParaModelo(depoimentoDTO);
            depoimento.setId(id);

            if (depoimentoDTO.getIdEgresso() != null) {
                Egresso filtro = Egresso.builder().id(depoimentoDTO.getIdEgresso()).build();
                List<Egresso> egressos = egressoService.buscarEgresso(filtro);
                if (egressos.isEmpty()) {
                    throw new RegraNegocioRunTime("Egresso não encontrado para o ID: " + depoimentoDTO.getIdEgresso());
                }
                depoimento.setEgresso(egressos.get(0));
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
    
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarDepoimentos() {
        try {
            List<Depoimento> depoimentos = depoimentoService.listarDepoimentos();
            List<DepoimentoDto> depoimentosDTO = depoimentos.stream()
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
            List<DepoimentoDto> depoimentosDTO = depoimentos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(depoimentosDTO);
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
    
    private Depoimento converterParaModelo(DepoimentoDto dto) {
        return Depoimento.builder()
                .id(dto.getId())
                .texto(dto.getTexto())
                .data(dto.getData())
                .status(Status.PENDENTE) 
                .build();
    }
    private DepoimentoDto converterParaDTO(Depoimento depoimento) {
        return DepoimentoDto.builder()
                .id(depoimento.getId())
                .texto(depoimento.getTexto())
                .data(depoimento.getData())
                .status(depoimento.getStatus())
                .idEgresso(depoimento.getEgresso().getId())
                .nomeEgresso(depoimento.getEgresso().getNome())
                .build();
    }
}
