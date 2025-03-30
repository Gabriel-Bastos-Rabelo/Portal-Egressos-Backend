package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(name = "data", nullable = false)
    private String data;

    @Column(name = "imagem_url", nullable = true)
    private String imagemUrl;

    @Column(name = "imagem_capa")
    private String imagemCapa;

    @Column(name = "link_noticia", nullable = false)
    private String linkNoticia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "autor", nullable = false)
    private String autor;
}
