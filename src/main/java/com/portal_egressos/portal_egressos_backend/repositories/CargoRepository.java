package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    List<Cargo> findAllByOrderByAnoInicioAsc();

    List<Cargo> findByEgressoIdOrderByAnoInicioAsc(Long egressoId);
}
