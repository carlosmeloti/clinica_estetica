package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoResponse(
        Long id,
        Long agendamentoId,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        LocalDateTime dataAtendimento,
        StatusAgendamento statusAgendamento,
        List<ProcedimentoRequestResponse> procedimentos,
        String relatoClinico,
        String notasProfissional,
        String observacoes,
        String orientacoesAoPaciente,
        String intercorrencias,
        String retornoRecomendado,
        Boolean finalizado,
        Integer numeroSessao,
        Double pesoPacienteKg,
        Double doseAplicadaMg,
        List<LocalAplicacaoRequestResponse> locaisAplicados,
        List<InsumoRequestResponse> consumos
) {}
