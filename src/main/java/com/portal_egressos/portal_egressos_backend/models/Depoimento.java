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
@Table(name = "depoimento", schema = "public")
public class Depoimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depoimento")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_egresso", referencedColumnName = "id_egresso", unique = true)
    private Egresso egresso;

    @Column(name = "texto", columnDefinition = "TEXT")
    private String texto;

    @Column(name = "data")
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
