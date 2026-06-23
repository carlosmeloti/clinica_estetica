package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByProfissionalIdAndDataHoraInicioBetween(
            Long profissionalId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Agendamento> findByStatus(StatusAgendamento status);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.profissional.id = :profissionalId " +
            "AND a.dataHoraInicio < :fim " +
            "AND a.dataHoraFim > :inicio " +
            "AND a.status != :statusCancelado")
    boolean existeAgendamentoNoMesmoHorario(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCancelado") StatusAgendamento statusCancelado
    );
}
