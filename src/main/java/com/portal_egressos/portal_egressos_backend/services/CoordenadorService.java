package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.repositories.CoordenadorRepository;

public class CoordenadorService {

    @Autowired
    private CoordenadorRepository coordenadorRepositorio;

    @Transactional
    public Coordenador salvarCoordenador(Coordenador coordenador) {
        verificarCoordenador(coordenador);
        Optional<Coordenador> coordenadorExistente = coordenadorRepositorio.findByUsuarioEmail(coordenador.getUsuario().getEmail());
        if (coordenadorExistente.isPresent()) {
            throw new RegraNegocioRunTime("Já existe um coordenador cadastrado com este email.");
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(coordenador.getUsuario().getSenha());

        coordenador.getUsuario().setSenha(senhaEncriptada);
        return coordenadorRepositorio.save(coordenador);
    }

    @Transactional
    public Coordenador atualizarCoordenador(Coordenador coordenador) {
        verificarCoordenadorId(coordenador);
        
        Coordenador coordenadorExistente = coordenadorRepositorio.findById(coordenador.getId()).get();
        if(coordenador.getUsuario().getSenha() != null && !coordenador.getUsuario().getSenha().isEmpty()){
            String senhaEncriptada = new BCryptPasswordEncoder().encode(coordenador.getUsuario().getSenha());
            coordenadorExistente.getUsuario().setSenha(senhaEncriptada);
        }
        if (coordenador.getNome() != null && !coordenador.getNome().isEmpty()){
            coordenadorExistente.setNome(coordenador.getNome());
        }
        if (coordenador.getDataCriacao() != null){
            coordenadorExistente.setDataCriacao(coordenador.getDataCriacao());
        }
        if (coordenador.getAtivo() != null){
            coordenadorExistente.setAtivo(coordenador.getAtivo());
        }
        
        return coordenadorRepositorio.save(coordenadorExistente);
    }

    @Transactional
    public void removerCoordenador(Coordenador coordenador) {
        verificarCoordenadorId(coordenador);
        coordenadorRepositorio.delete(coordenador);
    }

    public void verificarCoordenador(Coordenador coordenador) {
        if (coordenador == null) {
            throw new RegraNegocioRunTime("Coordenador inválido.");
        }
        if (coordenador.getNome() == null) {
            throw new RegraNegocioRunTime("Nome do coordenador deve ser informado.");
        }
        if (coordenador.getUsuario().getEmail() == null) {
            throw new RegraNegocioRunTime("Email do coordenador deve ser informado.");
        }
        if(coordenador.getUsuario().getSenha() == null){
            throw new RegraNegocioRunTime("Senha do coordenador deve ser informado.");
        }

    }

    public void verificarCoordenadorId(Coordenador coordenador) {
        if ((coordenador == null) || (coordenador.getId() == null)
                || !(coordenadorRepositorio.existsById(coordenador.getId()))) {
            throw new RegraNegocioRunTime("ID de coordenador é inválido.");
        }
    }

    public List<Coordenador> listarCoordenadores() {
        return coordenadorRepositorio.findAll();
    }

}
