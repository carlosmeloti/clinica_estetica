package com.cljtech.clinica.model.records;

import java.time.LocalDateTime;
import java.util.List;

public record EvolucaoEsteticaRequestResponse(Long id,
                                              Long evolucaoId,
                                              Long agendamentoId,
                                              Long pacienteId,
                                              Long profissionalId,
                                              LocalDateTime dataAtendimento,
                                              String relatoClinico,
                                              Integer numeroSessao,
                                              Double pesoPacienteKg,
                                              Double doseAplicadaMg,
                                              List<Long> locaisIds,
                                              List<InsumoRequestResponse> consumos)
{}
