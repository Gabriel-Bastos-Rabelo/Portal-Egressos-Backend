package com.portal_egressos.portal_egressos_backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email);
    
}
