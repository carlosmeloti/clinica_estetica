package com.cljtech.clinica.service;

import com.cljtech.clinica.model.records.AgendamentoRequestResponse;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {

    AgendamentoRequestResponse criar(AgendamentoRequestResponse agendamentoRequestResponse);
    List<AgendamentoRequestResponse> listarPorDiaEProfissional(Long profissionalId, LocalDate data);
    List<AgendamentoRequestResponse> listarTodos();
    AgendamentoRequestResponse atualizar(Long id, AgendamentoRequestResponse agendamentoRequestResponse);

}
