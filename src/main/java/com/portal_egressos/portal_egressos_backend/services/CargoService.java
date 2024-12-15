package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.repositories.CargoRepository;

import jakarta.transaction.Transactional;

@Service
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    private void verificarCargo(Cargo cargo){
        if(cargo == null){
            throw new RegraNegocioRunTime("O cargo não pode ser nulo.");
        }
        if(cargo.getDescricao() == null || cargo.getDescricao().isEmpty()){
            throw new RegraNegocioRunTime("A descrição do cargo é obrigatória.");
        }
        if(cargo.getLocal() == null || cargo.getLocal().isEmpty()){
            throw new RegraNegocioRunTime("O local do cargo é obrigatório.");
        }
        if(cargo.getAnoInicio() == null){
            throw new RegraNegocioRunTime("O ano de início do cargo é obrigatório.");
        }
    }

    @Transactional
    public Cargo salvar(Cargo cargo){
        verificarCargo(cargo);
        return cargoRepository.save(cargo);
    }

    public Cargo buscarPorId(Long id){
        return cargoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Cargo não encontrado para o ID: " + id));
    }


    public void remover(Long id){
        Cargo cargo = buscarPorId(id);
        cargoRepository.delete(cargo);
    }

    public List<Cargo> listarTodos() {
        return cargoRepository.findAllByOrderByAnoInicioAsc();
    }


    public List<Cargo> listarPorEgressoId(Long egressoId) {
        return cargoRepository.findByEgressoIdOrderByAnoInicioAsc(egressoId);
    }

    @Transactional
    public Cargo atualizar(Long id, Cargo cargoAtualizado) {
        Cargo cargoExistente = buscarPorId(id); 

        if (cargoAtualizado.getDescricao() != null && !cargoAtualizado.getDescricao().isEmpty()) {
            cargoExistente.setDescricao(cargoAtualizado.getDescricao());
        }
        if (cargoAtualizado.getLocal() != null && !cargoAtualizado.getLocal().isEmpty()) {
            cargoExistente.setLocal(cargoAtualizado.getLocal());
        }
        if (cargoAtualizado.getAnoInicio() != null) {
            cargoExistente.setAnoInicio(cargoAtualizado.getAnoInicio());
        }

        return cargoRepository.save(cargoExistente);
    }


}
