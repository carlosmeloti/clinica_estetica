package com.cljtech.clinica.model.records;

import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoRequest(
        Long agendamentoId,
        String relatoClinico,
        String notasProfissional,
        String observacoes,
        String orientacoesAoPaciente,
        String intercorrencias,
        String retornoRecomendado,
        List<Long> procedimentosIds,
        List<Long> locaisIds,
        List<InsumoRequestResponse> consumos,
        Integer numeroSessao,
        Double pesoPacienteKg,
        Double doseAplicadaMg
) {}
