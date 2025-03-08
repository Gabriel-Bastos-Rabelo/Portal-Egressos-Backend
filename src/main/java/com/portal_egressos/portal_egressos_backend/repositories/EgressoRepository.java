package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Egresso;

@Repository
public interface EgressoRepository extends JpaRepository<Egresso, Long> {

        Optional<Egresso> findByNome(String nome);

        Optional<Egresso> findByUsuarioEmail(String email);

        List<Egresso> findAllByStatus(Status status);

        Page<Egresso> findAllByStatus(Status status, Pageable pageable);

}