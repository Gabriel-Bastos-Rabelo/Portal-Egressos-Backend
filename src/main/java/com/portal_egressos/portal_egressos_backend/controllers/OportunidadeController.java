package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.dto.OportunidadeDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oportunidade")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private EgressoService egressoService;

    // Criar uma nova oportunidade (status PENDENTE)
    @PostMapping
    public ResponseEntity<?> salvarOportunidade(@RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            Egresso filtro = Egresso.builder().id(oportunidadeDTO.getIdEgresso()).build();

            List<Egresso> egressos = egressoService.buscarEgresso(filtro);
            if (egressos.isEmpty()) {
                throw new RegraNegocioRunTime("Egresso não encontrado para o ID: " + oportunidadeDTO.getIdEgresso());
            }

            Egresso egresso = egressos.get(0);
            Oportunidade oportunidade = converterParaModelo(oportunidadeDTO);
            oportunidade.setEgresso(egresso);

            Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade);
            return ResponseEntity.ok(converterParaDTO(oportunidadeSalva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarOportunidade(@PathVariable Long id, @RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            Oportunidade oportunidade = converterParaModelo(oportunidadeDTO);
            oportunidade.setId(id);

            // Verifica se o egresso foi enviado no DTO para atualizar também
            if (oportunidadeDTO.getIdEgresso() != null) {
                Egresso filtro = Egresso.builder().id(oportunidadeDTO.getIdEgresso()).build();
                List<Egresso> egressos = egressoService.buscarEgresso(filtro);
                if (egressos.isEmpty()) {
                    throw new RegraNegocioRunTime("Egresso não encontrado para o ID: " + oportunidadeDTO.getIdEgresso());
                }
                oportunidade.setEgresso(egressos.get(0));
            }

            Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidade);
            return ResponseEntity.ok(converterParaDTO(oportunidadeAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // // Atualizar uma oportunidade (incluindo status)
    // @PutMapping("/{id}")
    // public ResponseEntity<?> atualizarOportunidade(@PathVariable Long id, @RequestBody OportunidadeDTO oportunidadeDTO) {
    //     try {
    //         Oportunidade oportunidade = converterParaModelo(oportunidadeDTO);
    //         oportunidade.setId(id);

    //         Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidade);
    //         return ResponseEntity.ok(converterParaDTO(oportunidadeAtualizada));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

     @PutMapping("/{id}/status")
     public ResponseEntity<?> atualizarStatusOportunidade(@PathVariable("id") Long id, @RequestParam Status status) {
         try {
             Oportunidade oportunidade = Oportunidade.builder().id(id).status(status).build();
             Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarStatusOportunidade(oportunidade);
             return ResponseEntity.ok(converterParaDTO(oportunidadeAtualizada));
         } catch (Exception e) {
             return ResponseEntity.badRequest().body(e.getMessage());
        }
     }

    
    @GetMapping
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarOportunidade(@PathVariable Long id) {
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
