package com.cljtech.clinica.model.enuns;

public enum StatusAgendamento {
    PENDENTE("#FFA500"),   // Laranja
    CONFIRMADO("#28A745"), // Verde
    CANCELADO("#DC3545"),  // Vermelho
    FINALIZADO("#007BFF"), // Azul
    AUSENTE("#6C757D");    // Cinza

    private final String corHex;

    StatusAgendamento(String corHex) {
        this.corHex = corHex;
    }

    public String getCorHex() {
        return corHex;
    }
}
