package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.portal_egressos.portal_egressos_backend.enums.Status;

import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;

@Repository
public interface DepoimentoRepository extends JpaRepository<Depoimento, Long> {
    List<Depoimento> findAllByOrderByDataDesc();

    // List<Depoimento> findByEgresso(Egresso egresso);
    
    Optional<Depoimento> findByEgresso(Egresso egresso);

    List<Depoimento> findAllByStatus(Status status);
    
    // Optional<Depoimento> findByEgressoId(Long idEgresso);

}
