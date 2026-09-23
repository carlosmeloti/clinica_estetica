package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponse(
        Long id,
        Long agendamentoId,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        BigDecimal valorBruto,
        BigDecimal desconto,
        BigDecimal valorLiquido,
        BigDecimal valorPago,
        FormaPagamento formaPagamento,
        StatusPagamento status,
        LocalDateTime dataPagamento,
        String observacao,
        Long registradoPorId,
        String registradoPorNome,
        List<PagamentoItemResponse> itens
) {
}
