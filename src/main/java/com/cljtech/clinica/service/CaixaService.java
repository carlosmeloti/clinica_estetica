package com.cljtech.clinica.service;

import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import com.cljtech.clinica.model.records.ContaPendenteResponse;
import com.cljtech.clinica.model.records.PagamentoRequest;
import com.cljtech.clinica.model.records.PagamentoResponse;
import com.cljtech.clinica.model.records.RelatorioCaixaResponse;
import com.cljtech.clinica.model.records.SugestaoCobrancaResponse;

import java.time.LocalDate;
import java.util.List;

public interface CaixaService {

    PagamentoResponse registrarPagamento(PagamentoRequest request);

    PagamentoResponse buscarPorId(Long id);

    List<PagamentoResponse> listarPagamentos(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId,
            FormaPagamento formaPagamento,
            StatusPagamento status
    );

    PagamentoResponse estornar(Long id, String motivo);

    SugestaoCobrancaResponse sugerirCobranca(Long agendamentoId);

    List<ContaPendenteResponse> listarContasPendentes(LocalDate dataInicio, LocalDate dataFim, Long profissionalId);

    RelatorioCaixaResponse gerarRelatorio(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId
    );
}
