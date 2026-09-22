package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.ConsumoInsumo;
import com.cljtech.clinica.data.EvolucaoClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumoInsumoRepository extends JpaRepository<ConsumoInsumo, Long> {
    List<ConsumoInsumo> findByEvolucao(EvolucaoClinica evolucao);
}
