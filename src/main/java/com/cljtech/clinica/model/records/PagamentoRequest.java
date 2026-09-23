package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoRequest(
        @NotNull(message = "Agendamento é obrigatório.")
        Long agendamentoId,

        @DecimalMin(value = "0.0", inclusive = true, message = "Desconto não pode ser negativo.")
        BigDecimal desconto,

        @NotNull(message = "Valor pago é obrigatório.")
        @DecimalMin(value = "0.01", inclusive = true, message = "Valor pago deve ser maior que zero.")
        BigDecimal valorPago,

        @NotNull(message = "Forma de pagamento é obrigatória.")
        FormaPagamento formaPagamento,

        LocalDateTime dataPagamento,

        String observacao,

        /** Se vazio, os itens são montados a partir dos procedimentos do agendamento. */
        @Valid
        List<PagamentoItemRequest> itens,

        /** Se informado, sobrescreve o valor bruto calculado pelos itens. */
        @DecimalMin(value = "0.0", inclusive = true, message = "Valor bruto não pode ser negativo.")
        BigDecimal valorBruto,

        StatusPagamento status
) {
}
