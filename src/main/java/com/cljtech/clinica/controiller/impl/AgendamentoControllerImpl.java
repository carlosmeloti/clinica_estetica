package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.AgendamentoController;
import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
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
    public ResponseEntity<AgendamentoRequestResponse> criar(AgendamentoRequestResponse agendamentoRequestResponse) {
        return ResponseEntity.ok(agendamentoService.criar(agendamentoRequestResponse));
    }

    @Override
    public ResponseEntity<List<AgendamentoRequestResponse>> listarPorDiaEProfissional(Long profissionalId, LocalDate data) {
        List<AgendamentoRequestResponse> agendamentos = agendamentoService.listarPorDiaEProfissional(profissionalId, data);

        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agendamentos);
    }
}
