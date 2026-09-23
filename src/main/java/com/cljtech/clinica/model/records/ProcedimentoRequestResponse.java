package com.cljtech.clinica.model.records;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProcedimentoRequestResponse(
        Long id,
        @NotBlank(message = "Nome do procedimento é obrigatório.")
        String nome,
        @NotNull(message = "Preço do procedimento é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = true, message = "Preço não pode ser negativo.")
        BigDecimal precoSugerido,
        Integer duracaoMinutos
) {
}
