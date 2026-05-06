package com.cljtech.clinica.model.records;

import com.cljtech.clinica.data.Endereco;

import java.time.LocalDate;

public record PacienteRequestResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        Endereco endereco,
        String observacoesGerais,
        String tipoSanguineo,
        String nomeResponsavel
) {
}
