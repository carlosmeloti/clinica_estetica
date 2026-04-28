package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRespository extends JpaRepository<Paciente, Long> {
}
