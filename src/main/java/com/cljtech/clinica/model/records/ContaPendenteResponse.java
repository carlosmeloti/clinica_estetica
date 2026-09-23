package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Agendamento com saldo em aberto (útil para a fila do caixa). */
public record ContaPendenteResponse(
        Long agendamentoId,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        StatusAgendamento statusAgendamento,
        LocalDateTime dataHoraInicio,
        BigDecimal valorSugerido,
        BigDecimal totalPago,
        BigDecimal saldoEmAberto
) {
}
