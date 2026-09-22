package com.cljtech.clinica.model.records;

public record PacienteResumoResponse(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email
) {
}
