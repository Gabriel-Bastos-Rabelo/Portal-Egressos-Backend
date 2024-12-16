package com.portal_egressos.portal_egressos_backend.services;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.DepoimentoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepositorio;

    @Autowired
    private EgressoRepository egressoRepositorio;

    public Depoimento salvarDepoimento(Depoimento depoimento) {
        verificarDepoimento(depoimento);
        return depoimentoRepositorio.save(depoimento);
    }

    public Depoimento atualizarDepoimento(Depoimento depoimentoAtualizado) {
        Optional<Depoimento> depoimentoExistente = depoimentoRepositorio.findById(depoimentoAtualizado.getId());

        if (depoimentoExistente.isPresent()) {
            Depoimento depoimento = depoimentoExistente.get();
            if(!depoimentoAtualizado.getTexto().isEmpty()){
                depoimento.setTexto(depoimentoAtualizado.getTexto());
            }
            if(depoimentoAtualizado.getData() != null){
                depoimento.setData(depoimentoAtualizado.getData());
            }
            return depoimentoRepositorio.save(depoimento);
        }else {
            throw new RegraNegocioRunTime("Oportunidade não encontrada.");
        }
    }

    public Depoimento verificarDepoimentoId(Depoimento depoimento) {
        return depoimentoRepositorio.findById(depoimento.getId())
                .orElseThrow(() -> new RegraNegocioRunTime("Depoimento com ID " + depoimento.getId() + " não encontrado."));
    }

    public List<Depoimento> listarDepoimentos(){
        return depoimentoRepositorio.findAll();
    }

    public List<Depoimento> listarDepoimentoPorEgresso(Long idEgresso){
        Optional<Egresso> egressoExistente = egressoRepositorio.findById(idEgresso);

        if(!egressoExistente.isPresent()){
            throw new RegraNegocioRunTime("Egresso com id " + idEgresso + " não encontrado");
            
        }
        Egresso egresso = egressoExistente.get();
        return depoimentoRepositorio.findByEgresso(egresso);
    }

    public void verificarDepoimento(Depoimento depoimento) {
        if (depoimento.getTexto() == null || depoimento.getTexto().isEmpty()) {
            throw new RegraNegocioRunTime("A mensagem do depoimento deve ser informada.");
        }
        if (depoimento.getEgresso() == null || depoimento.getEgresso().getId() == null) {
            throw new RegraNegocioRunTime("O autor do depoimento deve ser informado.");
        }
    }
}
