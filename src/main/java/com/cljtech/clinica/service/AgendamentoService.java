package com.cljtech.clinica.service;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {

    AgendamentoResponse criar(AgendamentoRequest agendamentoRequest);
    List<AgendamentoResponse> listarPorDiaEProfissional(Long profissionalId, LocalDate data);
    List<AgendamentoResponse> listarTodos();
    AgendamentoResponse atualizar(Long id, AgendamentoRequest agendamentoRequest);

    List<AgendamentoResponse> listarPorStatus(StatusAgendamento status);
}
