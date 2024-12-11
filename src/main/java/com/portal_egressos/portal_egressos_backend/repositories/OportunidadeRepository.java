package com.portal_egressos.portal_egressos_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    List<Oportunidade> findByTituloContaining(String titulo);
    List<Oportunidade> findAllByOrderByDataPublicacaoDesc();
}
