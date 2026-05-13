package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
