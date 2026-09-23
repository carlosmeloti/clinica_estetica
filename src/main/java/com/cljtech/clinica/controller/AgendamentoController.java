package com.cljtech.clinica.controller;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public interface AgendamentoController {

    @PostMapping("/criar")
    ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest agendamentoRequest);

    @GetMapping("/listar-dia-profissional")
    ResponseEntity<List<AgendamentoResponse>> listarPorDiaEProfissional(
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );

    /**
     * Agenda por intervalo de datas.
     * Sem profissionalId → agenda geral (todos os profissionais).
     * Com profissionalId → agenda daquele profissional.
     */
    @GetMapping("/listar-agenda")
    ResponseEntity<List<AgendamentoResponse>> listarAgenda(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long profissionalId,
            @RequestParam(required = false) StatusAgendamento status
    );

    @GetMapping("/listar-por-status")
    ResponseEntity<List<AgendamentoResponse>> listarPorStatus(@RequestParam StatusAgendamento status);

    @GetMapping("/listar-todos")
    ResponseEntity<Page<AgendamentoResponse>> listarTodos(Pageable pageable);

    @PatchMapping("/atualizar/{id}")
    ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoRequest agendamentoRequest);

    @PatchMapping("/{id}/confirmar")
    ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id);

    @PatchMapping("/{id}/cancelar")
    ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id);

    @PatchMapping("/{id}/concluir")
    ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id);

    @PatchMapping("/{id}/nao-compareceu")
    ResponseEntity<AgendamentoResponse> naoCompareceu(@PathVariable Long id);
}
