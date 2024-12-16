package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.repositories.CoordenadorRepository;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;

import org.springframework.transaction.annotation.Transactional;

public class CoordenadorService {

    CoordenadorRepository coordenadorRepositorio;

    @Transactional
    public Coordenador salvarCoordenador(Coordenador coordenador) {
        verificarCoordenador(coordenador);
        return coordenadorRepositorio.save(coordenador);
    }

    @Transactional
    public Coordenador atualizarCoordenador(Coordenador coordenador) {
        verificarCoordenador(coordenador);
        verificarCoordenadorId(coordenador);
        if (coordenador.getUsuario().getSenha() == null || coordenador.getUsuario().getSenha().isEmpty()) {
            Coordenador coordenadorDb = coordenadorRepositorio.findById(coordenador.getId()).get();
            coordenador.getUsuario().setSenha(coordenadorDb.getUsuario().getSenha());
        }
        return coordenadorRepositorio.save(coordenador);
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
