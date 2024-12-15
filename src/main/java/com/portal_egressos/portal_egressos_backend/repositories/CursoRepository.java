package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNivel(String string);

    @Query (" select e from  Egresso e JOIN e.egressoCursos c WHERE c.curso.id = :cursoId ")
    List<Egresso> obterEgressosPorCurso(
        @Param("cursoId") Long cursoId
    );

}
