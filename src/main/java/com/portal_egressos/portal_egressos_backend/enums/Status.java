package com.portal_egressos.portal_egressos_backend.enums;

public enum Status {
    APPROVED("Approved"),
    NOT_APPROVED("Not Approved");

    private final String descricao;

    // Construtor
    Status(String descricao) {
        this.descricao = descricao;
    }

    // Método Getter
    public String getDescricao() {
        return descricao;
    }
}
