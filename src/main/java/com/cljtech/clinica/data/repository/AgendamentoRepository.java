package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByProfissionalIdAndDataHoraInicioBetween(
            Long profissionalId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Agendamento> findByStatus(StatusAgendamento status);
}
