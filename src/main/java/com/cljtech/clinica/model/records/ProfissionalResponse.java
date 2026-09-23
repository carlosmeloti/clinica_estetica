package com.cljtech.clinica.model.records;

public record ProfissionalResponse(
        Long id,
        String nome,
        String email,
        String login,
        String registroProfissional
) {
}
