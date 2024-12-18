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
    CargoRepository cargoRepositorio;
    private void verificarCargo(Cargo cargo){
        if(cargo == null){
            throw new RegraNegocioRunTime("Cargo inválido.");
        }
        if(cargo.getDescricao() == null || cargo.getDescricao().isEmpty()){
            throw new RegraNegocioRunTime("A descrição do cargo deve ser informada.");
        }
        if(cargo.getLocal() == null || cargo.getLocal().isEmpty()){
            throw new RegraNegocioRunTime("O local do cargo deve ser informado.");
        }
        if(cargo.getAnoInicio() == null){
            throw new RegraNegocioRunTime("O ano de início do cargo deve ser informado.");
        }
    }

    @Transactional
    public Cargo salvarCargo(Cargo cargo){
        verificarCargo(cargo);
        return cargoRepositorio.save(cargo);
    }

    public void verificarCargoId(Cargo cargo){
        if ((cargo == null) || (cargo.getId() == null)
                || !(cargoRepositorio.existsById(cargo.getId()))) {
            throw new RegraNegocioRunTime("ID do cargo é inválido.");
        }
    }

    @Transactional
    public void removerCargo(Cargo cargo){
        verificarCargoId(cargo);
        cargoRepositorio.delete(cargo);
    }

    public List<Cargo> listarCargos() {
        return cargoRepositorio.findAllByOrderByAnoInicioAsc();
    }


    public List<Cargo> listarCargoPorEgressoId(Long egressoId) {
        return cargoRepositorio.findByEgressoIdOrderByAnoInicioAsc(egressoId);
    }

    @Transactional
    public Cargo atualizarCargo(Cargo cargo) {
        verificarCargoId(cargo);
        Cargo cargoExistente = cargoRepositorio.findById(cargo.getId()).get(); 

        if (cargo.getDescricao() != null && !cargo.getDescricao().isEmpty()) {
            cargoExistente.setDescricao(cargo.getDescricao());
        }
        if (cargo.getLocal() != null && !cargo.getLocal().isEmpty()) {
            cargoExistente.setLocal(cargo.getLocal());
        }
        if (cargo.getAnoInicio() != null) {
            cargoExistente.setAnoInicio(cargo.getAnoInicio());
        }

        return cargoRepositorio.save(cargoExistente);
    }


}
