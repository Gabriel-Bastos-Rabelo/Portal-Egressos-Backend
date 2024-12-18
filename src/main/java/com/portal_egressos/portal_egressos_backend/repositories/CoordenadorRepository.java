package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Coordenador;

@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long>{
        
    Optional<Coordenador> findByNome(String nome);
    Optional<Coordenador> findByUsuarioEmail(String nome);

}
