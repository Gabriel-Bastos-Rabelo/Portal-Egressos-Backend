package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curso_egresso", schema = "public")
public class CursoEgresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_egresso")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_egresso", referencedColumnName = "id_egresso", nullable = false)
    private Egresso egresso;

    @ManyToOne
    @JoinColumn(name = "id_curso", referencedColumnName = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "ano_fim")
    private Integer anoFim;
}
