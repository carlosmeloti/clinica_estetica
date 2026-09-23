package com.cljtech.clinica.service;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {

    AgendamentoResponse criar(AgendamentoRequest agendamentoRequest);
    List<AgendamentoResponse> listarPorDiaEProfissional(Long profissionalId, LocalDate data);

    /**
     * Lista a agenda no intervalo informado.
     * Sem {@code profissionalId}: agenda geral (todos os profissionais).
     * Com {@code profissionalId}: agenda filtrada por profissional.
     */
    List<AgendamentoResponse> listarAgenda(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId,
            StatusAgendamento status
    );

    Page<AgendamentoResponse> listarTodos(Pageable pageable);
    AgendamentoResponse atualizar(Long id, AgendamentoRequest agendamentoRequest);

    AgendamentoResponse confirmar(Long id);
    AgendamentoResponse cancelar(Long id);
    AgendamentoResponse concluir(Long id);
    AgendamentoResponse naoCompareceu(Long id);
    AgendamentoResponse mudarStatus(Long id, StatusAgendamento novoStatus);

    List<AgendamentoResponse> listarPorStatus(StatusAgendamento status);
}
