package com.cljtech.clinica.model.records;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PagamentoItemRequest(
        Long procedimentoId,

        @NotBlank(message = "Descrição do item é obrigatória.")
        String descricao,

        @NotNull(message = "Valor unitário é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = true, message = "Valor unitário não pode ser negativo.")
        BigDecimal valorUnitario,

        @Min(value = 1, message = "Quantidade mínima é 1.")
        Integer quantidade
) {
}
