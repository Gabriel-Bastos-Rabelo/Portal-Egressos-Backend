package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.EgressoResponseDTO;
import com.portal_egressos.portal_egressos_backend.dto.OportunidadeDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;

@RestController
@RequestMapping("/api/oportunidade")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private EgressoService egressoService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarOportunidade(@RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            Oportunidade oportunidade = converterParaModelo(oportunidadeDTO);
            Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade,
                    oportunidadeDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(oportunidadeSalva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizarOportunidade(@PathVariable Long id,
            @RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            Oportunidade oportunidade = converterParaModelo(oportunidadeDTO);
            oportunidade.setId(id);
            if (oportunidadeDTO.getIdEgresso() != null) {
                Egresso egresso = egressoService.buscarPorId(oportunidadeDTO.getIdEgresso());
                oportunidade.setEgresso(egresso);
            }
            Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidade);
            return ResponseEntity.ok(converterParaDTO(oportunidadeAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("status/{id}")
    public ResponseEntity<?> atualizarStatusOportunidade(@PathVariable("id") Long id) {
        try {
            Status status = Status.APROVADO;

            Oportunidade oportunidade = Oportunidade.builder()
                    .id(id)
                    .status(status)
                    .build();
            Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarStatusOportunidade(oportunidade);
            return ResponseEntity.ok(converterParaDTO(oportunidadeAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarOportunidades() {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.listarTodasOportunidadesOrdenadasPorData();
            List<OportunidadeDTO> oportunidadesDTO = oportunidades.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(oportunidadesDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/aprovadas")
    public ResponseEntity<?> listarOportunidadesAprovadas() {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesAprovadasOrdenadasPorData();
            List<OportunidadeDTO> oportunidadesDTO = oportunidades.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(oportunidadesDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendentes")
    public ResponseEntity<?> listarPendentes() {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesPendentesOrdenadasPorData();
            List<OportunidadeDTO> oportunidadesDTO = oportunidades.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(oportunidadesDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("remover/{id}")
    public ResponseEntity<?> removerOportunidade(@PathVariable Long id) {
        try {
            Oportunidade oportunidade = oportunidadeService.buscarOportunidadePorId(id);
            oportunidadeService.removerOportunidade(oportunidade);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Oportunidade converterParaModelo(OportunidadeDTO dto) {
        return Oportunidade.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .local(dto.getLocal())
                .tipo(dto.getTipo())
                .dataPublicacao(dto.getDataPublicacao())
                .dataExpiracao(dto.getDataExpiracao())
                .salario(dto.getSalario())
                .link(dto.getLink())
                .status(Status.PENDENTE)
                .build();
    }

    private OportunidadeDTO converterParaDTO(Oportunidade oportunidade) {
        return OportunidadeDTO.builder()
                .id(oportunidade.getId())
                .titulo(oportunidade.getTitulo())
                .descricao(oportunidade.getDescricao())
                .local(oportunidade.getLocal())
                .tipo(oportunidade.getTipo())
                .dataPublicacao(oportunidade.getDataPublicacao())
                .dataExpiracao(oportunidade.getDataExpiracao())
                .salario(oportunidade.getSalario())
                .link(oportunidade.getLink())
                .status(oportunidade.getStatus())
                .idEgresso(oportunidade.getEgresso().getId())
                .nomeEgresso(oportunidade.getEgresso().getNome())
                .build();
    }
}
