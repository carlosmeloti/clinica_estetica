package com.cljtech.clinica.model.enuns;

public enum PerfilUsuario {

    ADMIN("Administrador"),
    MEDICO("Profissional de Saúde"),
    RECEPCAO("Recepcionista"),
    FINANCEIRO("Gestor Financeiro");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
