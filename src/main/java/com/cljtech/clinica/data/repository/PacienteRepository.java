package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
