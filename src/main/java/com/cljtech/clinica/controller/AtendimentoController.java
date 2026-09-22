package com.cljtech.clinica.controller;

import com.cljtech.clinica.model.records.AtendimentoRequest;
import com.cljtech.clinica.model.records.AtendimentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/atendimentos")
public interface AtendimentoController {

    @PostMapping
    ResponseEntity<AtendimentoResponse> criar(@RequestBody AtendimentoRequest request);

    @GetMapping("/{id}")
    ResponseEntity<AtendimentoResponse> buscarPorId(@PathVariable Long id);

    @GetMapping("/agendamento/{agendamentoId}")
    ResponseEntity<AtendimentoResponse> buscarPorAgendamento(@PathVariable Long agendamentoId);

    @GetMapping("/paciente/{pacienteId}")
    ResponseEntity<Page<AtendimentoResponse>> listarPorPaciente(@PathVariable Long pacienteId, Pageable pageable);

    @GetMapping("/profissional/{profissionalId}")
    ResponseEntity<Page<AtendimentoResponse>> listarPorProfissional(@PathVariable Long profissionalId, Pageable pageable);

    @PutMapping("/{id}")
    ResponseEntity<AtendimentoResponse> atualizar(@PathVariable Long id, @RequestBody AtendimentoRequest request);

    @PatchMapping("/{id}/finalizar")
    ResponseEntity<AtendimentoResponse> finalizar(@PathVariable Long id);
}
