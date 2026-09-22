package com.cljtech.clinica.service;

import com.cljtech.clinica.model.records.AtendimentoRequest;
import com.cljtech.clinica.model.records.AtendimentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AtendimentoService {
    AtendimentoResponse criarAtendimento(AtendimentoRequest request);
    AtendimentoResponse buscarPorId(Long id);
    AtendimentoResponse buscarPorAgendamento(Long agendamentoId);
    Page<AtendimentoResponse> listarPorPaciente(Long pacienteId, Pageable pageable);
    Page<AtendimentoResponse> listarPorProfissional(Long profissionalId, Pageable pageable);
    AtendimentoResponse atualizarAtendimento(Long id, AtendimentoRequest request);
    AtendimentoResponse finalizarAtendimento(Long id);
}
