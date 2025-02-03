package com.portal_egressos.portal_egressos_backend.enums;

public enum Status {
    APROVADO("Aprovado"),
    NAO_APROVADO("Não aprovado"),
    PENDENTE("Pendente");

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
