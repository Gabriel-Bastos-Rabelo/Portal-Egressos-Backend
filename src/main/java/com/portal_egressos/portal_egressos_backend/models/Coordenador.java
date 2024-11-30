package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coordenador", schema = "public")
public class Coordenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordenador")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "data_criacao", nullable = false)
    private java.time.LocalDateTime dataCriacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_curso", referencedColumnName = "id_curso")
    private Curso curso;
}
