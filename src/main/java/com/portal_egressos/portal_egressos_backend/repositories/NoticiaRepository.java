package com.portal_egressos.portal_egressos_backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    List<Noticia> findAllByStatusOrderByDataPublicacaoDesc(Status status);

    List<Noticia> findAllByOrderByDataPublicacaoDesc();

    List<Noticia> findByStatusAndDataPublicacaoAfterOrderByDataPublicacaoDesc(Status status, LocalDate dataLimite);
}
