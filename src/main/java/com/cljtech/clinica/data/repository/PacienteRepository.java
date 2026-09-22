package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p FROM Paciente p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:cpf IS NULL OR p.cpf = :cpf) AND " +
            "(:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Paciente> findByCriterios(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("email") String email,
            Pageable pageable);

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
