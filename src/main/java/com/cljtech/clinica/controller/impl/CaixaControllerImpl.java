package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.CaixaController;
import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import com.cljtech.clinica.model.records.ContaPendenteResponse;
import com.cljtech.clinica.model.records.PagamentoRequest;
import com.cljtech.clinica.model.records.PagamentoResponse;
import com.cljtech.clinica.model.records.RelatorioCaixaResponse;
import com.cljtech.clinica.model.records.SugestaoCobrancaResponse;
import com.cljtech.clinica.service.CaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CaixaControllerImpl implements CaixaController {

    private final CaixaService caixaService;

    @Override
    public ResponseEntity<PagamentoResponse> registrar(PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caixaService.registrarPagamento(request));
    }

    @Override
    public ResponseEntity<PagamentoResponse> buscar(Long id) {
        return ResponseEntity.ok(caixaService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<List<PagamentoResponse>> listar(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId,
            FormaPagamento formaPagamento,
            StatusPagamento status
    ) {
        List<PagamentoResponse> lista = caixaService.listarPagamentos(
                dataInicio, dataFim, profissionalId, formaPagamento, status
        );
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Override
    public ResponseEntity<PagamentoResponse> estornar(Long id, Map<String, String> body) {
        String motivo = body != null ? body.get("motivo") : null;
        return ResponseEntity.ok(caixaService.estornar(id, motivo));
    }

    @Override
    public ResponseEntity<SugestaoCobrancaResponse> sugerirCobranca(Long agendamentoId) {
        return ResponseEntity.ok(caixaService.sugerirCobranca(agendamentoId));
    }

    @Override
    public ResponseEntity<List<ContaPendenteResponse>> listarPendentes(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId
    ) {
        List<ContaPendenteResponse> lista = caixaService.listarContasPendentes(
                dataInicio, dataFim, profissionalId
        );
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Override
    public ResponseEntity<RelatorioCaixaResponse> relatorio(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId
    ) {
        return ResponseEntity.ok(caixaService.gerarRelatorio(dataInicio, dataFim, profissionalId));
    }
}
