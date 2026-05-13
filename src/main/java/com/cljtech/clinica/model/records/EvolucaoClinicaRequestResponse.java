package com.cljtech.clinica.model.records;

import java.time.LocalDateTime;

public record EvolucaoClinicaRequestResponse(Long id,
                Long agendamentoId,
                Long pacienteId,
                Long profissionalId,
                LocalDateTime dataRegistro,
                String relatoClinico,
                String diagnosticoHipotetico
) {
}
