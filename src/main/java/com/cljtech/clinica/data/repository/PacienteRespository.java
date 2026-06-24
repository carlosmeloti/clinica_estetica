package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRespository extends JpaRepository<Paciente, Long> {

    Page<Paciente> findByNomeOrCpfOrEmail(String nome, String cpf, String email, Pageable pageable);

}
