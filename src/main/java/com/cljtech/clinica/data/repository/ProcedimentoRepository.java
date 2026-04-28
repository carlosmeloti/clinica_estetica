package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
}
