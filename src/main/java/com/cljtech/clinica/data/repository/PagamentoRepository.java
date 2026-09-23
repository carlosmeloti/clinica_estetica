package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Pagamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {

    List<Pagamento> findByAgendamentoIdOrderByDataPagamentoAsc(Long agendamentoId);

    @Query("""
            SELECT COALESCE(SUM(p.valorPago), 0)
            FROM Pagamento p
            WHERE p.agendamento.id = :agendamentoId
              AND p.status <> :statusEstornado
            """)
    BigDecimal somarValorPagoPorAgendamento(
            @Param("agendamentoId") Long agendamentoId,
            @Param("statusEstornado") StatusPagamento statusEstornado
    );

    @Query("""
            SELECT DISTINCT p.agendamento.id
            FROM Pagamento p
            WHERE p.agendamento.id IN :agendamentoIds
              AND p.status <> :statusEstornado
            """)
    List<Long> findAgendamentoIdsComPagamentoAtivo(
            @Param("agendamentoIds") Collection<Long> agendamentoIds,
            @Param("statusEstornado") StatusPagamento statusEstornado
    );
}
