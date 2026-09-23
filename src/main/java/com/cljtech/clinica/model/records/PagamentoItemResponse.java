package com.cljtech.clinica.model.records;

import java.math.BigDecimal;

public record PagamentoItemResponse(
        Long id,
        Long procedimentoId,
        String descricao,
        BigDecimal valorUnitario,
        Integer quantidade,
        BigDecimal subtotal
) {
}
