package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumosRepository extends JpaRepository<Insumo, Long> {
}
