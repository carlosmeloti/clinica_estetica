package com.cljtech.clinica.model.enuns;

public enum StatusAgendamento {
    AGENDADO("#FFA500"),      // Laranja (antigo PENDENTE)
    CONFIRMADO("#28A745"),    // Verde
    EM_ATENDIMENTO("#17A2B8"), // Ciano
    CONCLUIDO("#007BFF"),     // Azul (antigo FINALIZADO)
    CANCELADO("#DC3545"),     // Vermelho
    NAO_COMPARECEU("#6C757D"); // Cinza (antigo AUSENTE)

    private final String corHex;

    StatusAgendamento(String corHex) {
        this.corHex = corHex;
    }

    public String getCorHex() {
        return corHex;
    }
}
