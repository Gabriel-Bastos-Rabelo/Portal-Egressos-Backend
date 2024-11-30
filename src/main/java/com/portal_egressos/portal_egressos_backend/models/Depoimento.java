package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "depoimento", schema = "public")
public class Depoimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depoimento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_egresso", referencedColumnName = "id_egresso", nullable = false)
    private Egresso egresso;

    @ManyToOne
    @JoinColumn(name = "id_coordenador", referencedColumnName = "id_coordenador")
    private Coordenador coordenador;

    @Column(name = "texto")
    private String texto;

    @Column(name = "data")
    private LocalDate data;
}
