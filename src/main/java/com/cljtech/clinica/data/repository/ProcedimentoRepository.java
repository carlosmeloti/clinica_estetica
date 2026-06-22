package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
    List<Procedimento> findAllByIdIn(List<Long> ids);
}
