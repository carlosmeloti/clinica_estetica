package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RelatorioCaixaResponse(
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal totalBruto,
        BigDecimal totalDescontos,
        BigDecimal totalRecebido,
        long quantidadePagamentos,
        BigDecimal ticketMedio,
        long quantidadeContasPendentes,
        BigDecimal valorPendente,
        List<TotalPorChave> porFormaPagamento,
        List<TotalPorChave> porProcedimento,
        List<TotalPorChave> porProfissional,
        List<TotalPorDia> porDia,
        List<PagamentoResponse> lancamentos
) {
    public record TotalPorChave(String chave, String rotulo, BigDecimal total, long quantidade) {
        public static TotalPorChave deForma(FormaPagamento forma, BigDecimal total, long quantidade) {
            return new TotalPorChave(forma.name(), forma.name(), total, quantidade);
        }
    }

    public record TotalPorDia(LocalDate data, BigDecimal total, long quantidade) {
    }
}
