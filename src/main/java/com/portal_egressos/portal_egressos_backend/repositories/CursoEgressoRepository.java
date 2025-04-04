package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;

@Repository
public interface CursoEgressoRepository extends JpaRepository<CursoEgresso, Long> {

    Optional<CursoEgresso> findByEgressoId(Long egressoId);


}
