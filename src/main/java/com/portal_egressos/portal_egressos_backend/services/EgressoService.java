package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

public class EgressoService {

    @Autowired
    private EgressoRepository egressoRepositorio;

    @Transactional
    public Egresso salvarEgresso(Egresso egresso) {
        verificarEgresso(egresso);
        Optional<Egresso> egressoExistente = egressoRepositorio.findByUsuarioEmail(egresso.getUsuario().getEmail());
        if (egressoExistente.isPresent()) {
            throw new RegraNegocioRunTime("Já existe um egresso cadastrado com este email.");
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(egresso.getUsuario().getSenha());

        egresso.getUsuario().setSenha(senhaEncriptada);
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
        verificarEgressoId(egresso);

        Egresso egressoExistente = egressoRepositorio.findById(egresso.getId()).get();

        if(egresso.getUsuario().getSenha() != null && !egresso.getUsuario().getSenha().isEmpty()){
            String senhaEncriptada = new BCryptPasswordEncoder().encode(egresso.getUsuario().getSenha());
            egressoExistente.getUsuario().setSenha(senhaEncriptada);
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
        if(egresso.getUsuario().getSenha() == null){
            throw new RegraNegocioRunTime("Senha do egresso deve ser informado.");
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
