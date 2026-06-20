package com.cljtech.clinica.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumos_insumos", schema = "clinica")
@Getter
@Setter
@NoArgsConstructor
public class ConsumoInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolucao_id", nullable = false)
    private EvolucaoClinica evolucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "quantidade_usada", nullable = false)
    private Double quantidadeUsada;

    @Column(name = "data_consumo", nullable = false)
    private LocalDateTime dataConsumo = LocalDateTime.now();
}
