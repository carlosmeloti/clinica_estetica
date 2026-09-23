package com.cljtech.clinica.model.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Sugestão de cobrança a partir dos procedimentos do agendamento. */
public record SugestaoCobrancaResponse(
        Long agendamentoId,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        BigDecimal valorPrevistoAgendamento,
        BigDecimal valorSugerido,
        BigDecimal totalJaPago,
        BigDecimal saldoEmAberto,
        List<PagamentoItemResponse> itensSugeridos,
        LocalDateTime dataHoraInicio
) {
}
