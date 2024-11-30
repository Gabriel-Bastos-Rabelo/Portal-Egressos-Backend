package com.portal_egressos.portal_egressos_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.models.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Object> {

}
