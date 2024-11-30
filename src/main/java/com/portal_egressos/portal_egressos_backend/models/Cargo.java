package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cargo", schema = "public")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_egresso", referencedColumnName = "id_egresso", nullable = false)
    private Egresso egresso;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "local", nullable = false)
    private String local;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "ano_fim")
    private Integer anoFim;
}
