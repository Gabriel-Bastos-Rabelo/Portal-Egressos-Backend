package com.portal_egressos.portal_egressos_backend.repositories;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    
}
