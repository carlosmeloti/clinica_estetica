package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.AgendamentoController;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
import com.cljtech.clinica.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AgendamentoControllerImpl implements AgendamentoController {

    private final AgendamentoService agendamentoService;
    @Override
    public ResponseEntity<AgendamentoResponse> criar(AgendamentoRequest agendamentoRequest) {
        return ResponseEntity.ok(agendamentoService.criar(agendamentoRequest));
    }

    @Override
    public ResponseEntity<List<AgendamentoResponse>> listarPorDiaEProfissional(Long profissionalId, LocalDate data) {
        List<AgendamentoResponse> agendamentos = agendamentoService.listarPorDiaEProfissional(profissionalId, data);

        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @Override
    public ResponseEntity<List<AgendamentoResponse>> listarPorStatus(StatusAgendamento status) {

        List<AgendamentoResponse> agendamentos = agendamentoService.listarPorStatus(status);

        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @Override
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        List<AgendamentoResponse> agendamentos = agendamentoService.listarTodos();
        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamentos);
    }

    @Override
    public ResponseEntity<AgendamentoResponse> atualizar(Long id, AgendamentoRequest agendamentoRequest) {
        AgendamentoResponse agendamento = agendamentoService.atualizar(id, agendamentoRequest);
       return ResponseEntity.ok(agendamento);
    }
}
