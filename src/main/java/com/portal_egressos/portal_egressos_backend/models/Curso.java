package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curso", schema = "public")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nivel", nullable = false)
    private String nivel;
}
