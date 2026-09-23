package com.cljtech.clinica.controller;

import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import com.cljtech.clinica.model.records.ContaPendenteResponse;
import com.cljtech.clinica.model.records.PagamentoRequest;
import com.cljtech.clinica.model.records.PagamentoResponse;
import com.cljtech.clinica.model.records.RelatorioCaixaResponse;
import com.cljtech.clinica.model.records.SugestaoCobrancaResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/caixa")
public interface CaixaController {

    @PostMapping("/pagamentos")
    ResponseEntity<PagamentoResponse> registrar(@RequestBody @Valid PagamentoRequest request);

    @GetMapping("/pagamentos/{id}")
    ResponseEntity<PagamentoResponse> buscar(@PathVariable Long id);

    @GetMapping("/pagamentos")
    ResponseEntity<List<PagamentoResponse>> listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long profissionalId,
            @RequestParam(required = false) FormaPagamento formaPagamento,
            @RequestParam(required = false) StatusPagamento status
    );

    @PatchMapping("/pagamentos/{id}/estornar")
    ResponseEntity<PagamentoResponse> estornar(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body
    );

    @GetMapping("/sugestao/{agendamentoId}")
    ResponseEntity<SugestaoCobrancaResponse> sugerirCobranca(@PathVariable Long agendamentoId);

    @GetMapping("/pendentes")
    ResponseEntity<List<ContaPendenteResponse>> listarPendentes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long profissionalId
    );

    /**
     * Relatório agregado para gráficos e impressão (diário/semanal/mensal/período livre).
     * Front envia dataInicio/dataFim conforme o período escolhido.
     */
    @GetMapping("/relatorios")
    ResponseEntity<RelatorioCaixaResponse> relatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long profissionalId
    );
}
