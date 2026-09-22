package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.AtendimentoController;
import com.cljtech.clinica.model.records.AtendimentoRequest;
import com.cljtech.clinica.model.records.AtendimentoResponse;
import com.cljtech.clinica.service.AtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AtendimentoControllerImpl implements AtendimentoController {

    private final AtendimentoService atendimentoService;

    @Override
    public ResponseEntity<AtendimentoResponse> criar(AtendimentoRequest request) {
        return ResponseEntity.ok(atendimentoService.criarAtendimento(request));
    }

    @Override
    public ResponseEntity<AtendimentoResponse> buscarPorId(Long id) {
        return ResponseEntity.ok(atendimentoService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<AtendimentoResponse> buscarPorAgendamento(Long agendamentoId) {
        return ResponseEntity.ok(atendimentoService.buscarPorAgendamento(agendamentoId));
    }

    @Override
    public ResponseEntity<Page<AtendimentoResponse>> listarPorPaciente(Long pacienteId, Pageable pageable) {
        return ResponseEntity.ok(atendimentoService.listarPorPaciente(pacienteId, pageable));
    }

    @Override
    public ResponseEntity<Page<AtendimentoResponse>> listarPorProfissional(Long profissionalId, Pageable pageable) {
        return ResponseEntity.ok(atendimentoService.listarPorProfissional(profissionalId, pageable));
    }

    @Override
    public ResponseEntity<AtendimentoResponse> atualizar(Long id, AtendimentoRequest request) {
        return ResponseEntity.ok(atendimentoService.atualizarAtendimento(id, request));
    }

    @Override
    public ResponseEntity<AtendimentoResponse> finalizar(Long id) {
        return ResponseEntity.ok(atendimentoService.finalizarAtendimento(id));
    }
}
