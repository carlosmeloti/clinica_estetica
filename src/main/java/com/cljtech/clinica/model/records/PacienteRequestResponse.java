package com.cljtech.clinica.model.records;

import com.cljtech.clinica.data.Endereco;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PacienteRequestResponse(
        Long id,
        @NotBlank(message = "{paciente.nome.obrigatorio}")
        String nome,
        @NotBlank(message = "{paciente.cpf.obrigatorio}")
        String cpf,
        @Email(message = "{paciente.email.invalido}")
        @NotBlank(message = "{paciente.email.obrigatorio}")
        String email,
        @NotBlank(message = "{paciente.telefone.obrigatorio}")
        String telefone,
        @NotNull(message = "{paciente.dataNascimento.obrigatoria}")
        LocalDate dataNascimento,
        Endereco endereco,
        String observacoesGerais,
        String tipoSanguineo,
        String nomeResponsavel
) {
}
