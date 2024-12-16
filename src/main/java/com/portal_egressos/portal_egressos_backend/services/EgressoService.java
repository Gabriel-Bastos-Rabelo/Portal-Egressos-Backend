package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.models.Egresso;

public class EgressoService {

    EgressoRepository egressoRepositorio;

    @Transactional
    public Egresso salvarEgresso(Egresso egresso) {
        verificarEgresso(egresso);
        return egressoRepositorio.save(egresso);
    }

    public List<Egresso> buscarEgresso(Egresso filtro) {
        Example<Egresso> example = Example.of(filtro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING));

        return egressoRepositorio.findAll(example);
    }

    @Transactional
    public Egresso atualizarEgresso(Egresso egresso) {
        verificarEgresso(egresso);
        verificarEgressoId(egresso);

        Egresso egressoExistente = egressoRepositorio.findById(egresso.getId()).get();

        if (egresso.getUsuario().getSenha() == null || egresso.getUsuario().getSenha().isEmpty()) {
            egresso.getUsuario().setSenha(egressoExistente.getUsuario().getSenha());
        }
        if (egresso.getNome() != null && !egresso.getNome().isEmpty()){
            egressoExistente.setNome(egresso.getNome());
        }
        if (egresso.getDescricao() != null && !egresso.getDescricao().isEmpty()){
            egressoExistente.setDescricao(egresso.getDescricao());
        }
        if (egresso.getFoto() != null && !egresso.getFoto().isEmpty()){
            egressoExistente.setFoto(egresso.getFoto());
        }
        if (egresso.getLinkedin() != null && !egresso.getLinkedin().isEmpty()){
            egressoExistente.setLinkedin(egresso.getLinkedin());
        }
        if (egresso.getInstagram() != null && !egresso.getInstagram().isEmpty()){
            egressoExistente.setInstagram(egresso.getInstagram());
        }
        if (egresso.getCurriculo() != null && !egresso.getCurriculo().isEmpty()){
            egressoExistente.setCurriculo(egresso.getCurriculo());
        }

        return egressoRepositorio.save(egressoExistente);
    }

    @Transactional
    public void removerEgresso(Egresso egresso) {
        verificarEgressoId(egresso);
        egressoRepositorio.delete(egresso);
    }

    public void verificarEgresso(Egresso egresso) {
        if (egresso == null) {
            throw new RegraNegocioRunTime("Egresso inválido.");
        }
        if (egresso.getNome() == null) {
            throw new RegraNegocioRunTime("Nome do egresso deve ser informado.");
        }
        if (egresso.getUsuario().getEmail() == null) {
            throw new RegraNegocioRunTime("Email do egresso deve ser informado.");
        }

    }

    public void verificarEgressoId(Egresso egresso) {
        if ((egresso == null) || (egresso.getId() == null) || !(egressoRepositorio.existsById(egresso.getId()))) {
            throw new RegraNegocioRunTime("ID de egresso é inválido.");
        }
    }

    public List<Egresso> listarEgressos() {
        return egressoRepositorio.findAll();
    }

}
