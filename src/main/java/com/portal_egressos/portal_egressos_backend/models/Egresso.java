package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter @Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "egresso", schema = "public")
public class Egresso{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_egresso")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "foto")
    private String foto;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "curriculo")
    private String curriculo;

    @ManyToOne
    @JoinColumn(name = "id_coordenador", referencedColumnName = "id_coordenador")
    private Coordenador coordenador;



    
}