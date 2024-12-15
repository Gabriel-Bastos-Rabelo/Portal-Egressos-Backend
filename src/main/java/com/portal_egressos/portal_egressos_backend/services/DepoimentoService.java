package com.portal_egressos.portal_egressos_backend.services;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.DepoimentoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    // Salvar Depoimento
    public Depoimento salvarDepoimento(Depoimento depoimento) {
        if (depoimento.getTexto() == null || depoimento.getTexto().isEmpty()) {
            throw new RegraNegocioRunTime("A mensagem do depoimento não pode estar vazia.");
        }
        if (depoimento.getEgresso() == null || depoimento.getEgresso().getId() == null) {
            throw new RegraNegocioRunTime("É necessário informar o egresso autor do depoimento.");
        }

        Egresso egresso = egressoRepository.findById(depoimento.getEgresso().getId())
                .orElseThrow(() -> new RegraNegocioRunTime(
                        "Egresso com ID " + depoimento.getEgresso().getId() + " não encontrado."));

        depoimento.setEgresso(egresso);
        return depoimentoRepository.save(depoimento);

    }

    // Atualizar Depoimento

    public Depoimento atualizarDepoimento(Long id, Depoimento novoDepoimento) {
        return depoimentoRepository.findById(id)
                .map(depoimento -> {
                    if (novoDepoimento.getTexto() == null || novoDepoimento.getTexto().isEmpty()) {
                        throw new RegraNegocioRunTime("A mensagem do depoimento não pode estar vazia.");
                    }
                    if (novoDepoimento.getEgresso() != null && novoDepoimento.getEgresso().getId() != null) {
                        Egresso egresso = egressoRepository.findById(novoDepoimento.getEgresso().getId())
                                .orElseThrow(() -> new RegraNegocioRunTime(
                                        "Egresso com ID " + novoDepoimento.getEgresso().getId() + " não encontrado."));
                        depoimento.setEgresso(egresso);
                    }

                    depoimento.setTexto(novoDepoimento.getTexto());
                    return depoimentoRepository.save(depoimento);
                })
                .orElseThrow(() -> new RegraNegocioRunTime("Depoimento com ID " + id + " não encontrado."));
    }

    // Verificar Depoimento

    public Depoimento verificarDepoimento(Long id) {
        return depoimentoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Depoimento com ID " + id + " não encontrado."));
    }

}
