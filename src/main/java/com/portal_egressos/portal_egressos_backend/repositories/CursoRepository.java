package com.portal_egressos.portal_egressos_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Object> {

}
