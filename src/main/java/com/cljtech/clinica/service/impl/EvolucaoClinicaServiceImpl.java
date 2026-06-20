package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.*;
import com.cljtech.clinica.data.repository.*;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.EvolucaoEsteticaRequestResponse;
import com.cljtech.clinica.service.EvolucaoEsteticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EvolucaoClinicaServiceImpl implements EvolucaoEsteticaService {

    private final EvolucaoEsteticaRepository evolucaoEsteticaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ConsumoInsumoRepository consumoInsumoRepository;
    private final InsumosRepository insumosRepository;
    private final EntityMapper entityMapper;
    private final LocalAplicacaoRepository localAplicacaoRepository;

    @Override
    public EvolucaoEsteticaRequestResponse criar(EvolucaoEsteticaRequestResponse request) {

        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento id " + request.agendamentoId() + " não encontrado."));

        if (agendamento.getProfissional() == null) {
            throw new IllegalStateException("Este agendamento não possui um profissional atribuído no banco de dados.");
        }

        EvolucaoEstetica evolucao = new EvolucaoEstetica();
        evolucao.setAgendamento(agendamento);
        evolucao.setPaciente(agendamento.getPaciente());
        evolucao.setProfissional(agendamento.getProfissional());

        evolucao.setDataRegistro(LocalDateTime.now());
        evolucao.setRelatoClinico(request.relatoClinico());
        evolucao.setNumeroSessao(request.numeroSessao());
        evolucao.setPesoPacienteKg(request.pesoPacienteKg());
        evolucao.setDoseAplicadaMg(request.doseAplicadaMg());

        if (request.locaisIds() != null && !request.locaisIds().isEmpty()) {
            List<LocalAplicacao> locais = localAplicacaoRepository.findAllById(request.locaisIds());
            evolucao.setLocaisAplicados(locais);
        }
        EvolucaoEstetica evolucaoSalva = evolucaoEsteticaRepository.save(evolucao);
        agendamento.setStatus(StatusAgendamento.FINALIZADO);
        agendamentoRepository.save(agendamento);


        if (request.consumos() != null && !request.consumos().isEmpty()) {
            request.consumos().forEach(consumoDto -> {
                Insumo insumo = insumosRepository.findById(consumoDto.id())
                        .orElseThrow(() -> new RuntimeException("Insumo id " + consumoDto.id() + " não encontrado."));

                // Registra o histórico de consumo
                ConsumoInsumo consumo = new ConsumoInsumo();
                consumo.setEvolucao(evolucaoSalva);
                consumo.setInsumo(insumo);
                consumo.setQuantidadeUsada(consumoDto.quantidadeUsada());
                consumoInsumoRepository.save(consumo);

                // Deduz a quantidade do estoque do produto
                double estoqueAtualizado = insumo.getQuantidadeEstoque() - consumoDto.quantidadeUsada();
                if (estoqueAtualizado < 0) {
                    // Opcional: Lançar erro ou apenas deixar o estoque negativo se a clínica permitir trabalhar assim
                    // throw new RuntimeException("Estoque insuficiente para o insumo: " + insumo.getNome());
                }
                insumo.setQuantidadeEstoque(estoqueAtualizado);
                insumosRepository.save(insumo);
            });
        }
        return entityMapper.toEvolucaoClinicaRequestRessponse(evolucaoSalva);
    }
}
