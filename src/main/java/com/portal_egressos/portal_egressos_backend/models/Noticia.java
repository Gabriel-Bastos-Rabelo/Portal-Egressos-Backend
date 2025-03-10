package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.portal_egressos.portal_egressos_backend.enums.Status;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "noticia", schema = "public")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_noticia")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_egresso", referencedColumnName = "id_egresso", nullable = false)
    private Egresso egresso;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Column(name = "data_extracao", nullable = false)
    private LocalDate dataExtracao;

    @Column(name = "imagem_capa")
    private String imagemCapa;

    @Column(name = "link_noticia", nullable = false)
    private String linkNoticia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;


}
