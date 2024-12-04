package com.portal_egressos.portal_egressos_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", schema = "public")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;
}
