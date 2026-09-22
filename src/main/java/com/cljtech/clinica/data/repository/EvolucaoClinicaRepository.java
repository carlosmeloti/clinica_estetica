package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.EvolucaoClinica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvolucaoClinicaRepository extends JpaRepository<EvolucaoClinica, Long> {

    Optional<EvolucaoClinica> findByAgendamentoId(Long agendamentoId);

    Page<EvolucaoClinica> findByPacienteId(Long pacienteId, Pageable pageable);

    Page<EvolucaoClinica> findByProfissionalId(Long profissionalId, Pageable pageable);

    boolean existsByAgendamentoId(Long agendamentoId);
}

