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

    public boolean efetuarLogin(String email, String senha) {
        return true;
    }

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
        if (egresso.getUsuario().getSenha() == null || egresso.getUsuario().getSenha().isEmpty()) {
            Egresso egressoDb = egressoRepositorio.findById(egresso.getId()).get();
            egresso.getUsuario().setSenha(egressoDb.getUsuario().getSenha());
        }
        return egressoRepositorio.save(egresso);
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
