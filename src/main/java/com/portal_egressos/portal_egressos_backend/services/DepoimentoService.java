package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.DepoimentoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.enums.Status;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepositorio;

    @Autowired
    private EgressoRepository egressoRepositorio;

    @Transactional
    public Depoimento salvarDepoimento(Depoimento depoimento) {
        verificarDepoimento(depoimento);
    
        Optional<Depoimento> depoimentoExistente = depoimentoRepositorio.findByEgresso(depoimento.getEgresso());
        
        if (depoimentoExistente.isPresent()) {
            throw new RegraNegocioRunTime("O egresso já possui um depoimento cadastrado.");
        }
    
        return depoimentoRepositorio.save(depoimento);
    }
    

    @Transactional
    public Depoimento atualizarDepoimento(Depoimento depoimentoAtualizado) {
        verificarDepoimentoId(depoimentoAtualizado);
        Depoimento depoimentoExistente = depoimentoRepositorio.findById(depoimentoAtualizado.getId()).get();

        if (!depoimentoAtualizado.getTexto().isEmpty()) {
            depoimentoExistente.setTexto(depoimentoAtualizado.getTexto());
        }
        if (depoimentoAtualizado.getData() != null) {
            depoimentoExistente.setData(depoimentoAtualizado.getData());
        }
        if (depoimentoAtualizado.getStatus() != null) {
            depoimentoExistente.setStatus(depoimentoAtualizado.getStatus());
        }
        return depoimentoRepositorio.save(depoimentoExistente);
    }

    @Transactional
    public Depoimento atualizarStatusDepoimento(Depoimento depoimentoAtualizado) {
        if (depoimentoAtualizado.getId() == null) {
            throw new RegraNegocioRunTime("O id do depoimento é obrigatório.");
        }

        if (depoimentoAtualizado.getStatus() == null) {
            throw new RegraNegocioRunTime("O status do depoimento é obrigatório.");
        }

        Depoimento depoimento = depoimentoRepositorio.findById(depoimentoAtualizado.getId())
                .orElseThrow(() -> new RegraNegocioRunTime(
                        "Depoimento não encontrado para o ID: " + depoimentoAtualizado.getId()));

        depoimento.setStatus(depoimentoAtualizado.getStatus());

        return depoimentoRepositorio.save(depoimento);
    }

    public void verificarDepoimentoId(Depoimento depoimento) {
        if ((depoimento == null) || (depoimento.getId() == null)
                || !(depoimentoRepositorio.existsById(depoimento.getId()))) {
            throw new RegraNegocioRunTime("ID de depoimento é inválido.");
        }
    }

    @Transactional
    public void removerDepoimento(Long id) {
        Depoimento depoimento = depoimentoRepositorio.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Depoimento não encontrado para o ID: " + id));
        depoimentoRepositorio.delete(depoimento);
    }

    public List<Depoimento> listarDepoimentos() {
        return depoimentoRepositorio.findAll();
    }

    public Optional<Depoimento> buscarDepoimentoPorEgresso(Long idEgresso) {
        Optional<Egresso> egressoOpt = egressoRepositorio.findById(idEgresso);
    
        if (egressoOpt.isEmpty()) {
            throw new RegraNegocioRunTime("Egresso com id " + idEgresso + " não encontrado");
        }
    
        Egresso egresso = egressoOpt.get();
    
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

    public List<Depoimento> listarDepoimentosAprovados() {
        List<Depoimento> depoimentosAprovados = depoimentoRepositorio.findAllByStatus(Status.APROVADO);
        if (depoimentosAprovados.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhum depoimento encontrado com status APROVADO.");
        }
        return depoimentosAprovados;
    }

    public List<Depoimento> listarDepoimentosPendentes() {
        List<Depoimento> depoimentosPendentes = depoimentoRepositorio.findAllByStatus(Status.PENDENTE);
        if (depoimentosPendentes.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhum depoimento encontrado com status PENDENTE.");
        }
        return depoimentosPendentes;
    }
    
}
