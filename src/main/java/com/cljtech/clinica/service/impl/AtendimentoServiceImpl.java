package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.*;
import com.cljtech.clinica.data.repository.*;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AtendimentoRequest;
import com.cljtech.clinica.model.records.AtendimentoResponse;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.service.AgendamentoService;
import com.cljtech.clinica.service.AtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AtendimentoServiceImpl implements AtendimentoService {

    private final EvolucaoClinicaRepository evolucaoRepository;
    private final EvolucaoEsteticaRepository evolucaoEsteticaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final LocalAplicacaoRepository localAplicacaoRepository;
    private final ConsumoInsumoRepository consumoInsumoRepository;
    private final InsumosRepository insumosRepository;
    private final EntityMapper entityMapper;
    private final AgendamentoService agendamentoService;

    @Override
    public AtendimentoResponse criarAtendimento(AtendimentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado."));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RegraNegocioException("Não é possível iniciar atendimento para um agendamento cancelado.");
        }

        if (evolucaoRepository.existsByAgendamentoId(request.agendamentoId())) {
            throw new RegraNegocioException("Já existe um atendimento para este agendamento.");
        }

        EvolucaoEstetica evolucao = new EvolucaoEstetica();
        evolucao.setAgendamento(agendamento);
        evolucao.setPaciente(agendamento.getPaciente());
        evolucao.setProfissional(agendamento.getProfissional());
        evolucao.setDataRegistro(LocalDateTime.now());
        
        mapRequestToEntity(request, evolucao);

        EvolucaoEstetica salva = evolucaoEsteticaRepository.save(evolucao);
        
        processarConsumos(request.consumos(), salva);

        agendamentoService.mudarStatus(agendamento.getId(), StatusAgendamento.EM_ATENDIMENTO);

        return mapToResponse(salva);
    }

    @Override
    public AtendimentoResponse buscarPorId(Long id) {
        EvolucaoClinica evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado."));
        return mapToResponse(evolucao);
    }

    @Override
    public AtendimentoResponse buscarPorAgendamento(Long agendamentoId) {
        EvolucaoClinica evolucao = evolucaoRepository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado para este agendamento."));
        return mapToResponse(evolucao);
    }

    @Override
    public Page<AtendimentoResponse> listarPorPaciente(Long pacienteId, Pageable pageable) {
        return evolucaoRepository.findByPacienteId(pacienteId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<AtendimentoResponse> listarPorProfissional(Long profissionalId, Pageable pageable) {
        return evolucaoRepository.findByProfissionalId(profissionalId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public AtendimentoResponse atualizarAtendimento(Long id, AtendimentoRequest request) {
        EvolucaoEstetica evolucao = evolucaoEsteticaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado."));

        if (Boolean.TRUE.equals(evolucao.getFinalizado())) {
            throw new RegraNegocioException("Não é possível editar um atendimento finalizado.");
        }

        mapRequestToEntity(request, evolucao);
        
        // Limpar consumos anteriores e processar novos se necessário
        // Nota: Em um cenário real, poderíamos comparar e atualizar, mas aqui faremos o simples: substituir
        List<ConsumoInsumo> consumosAntigos = consumoInsumoRepository.findByEvolucao(evolucao);
        // Estornar estoque se necessário antes de deletar
        consumosAntigos.forEach(c -> {
            Insumo insumo = c.getInsumo();
            insumo.setQuantidadeEstoque(insumo.getQuantidadeEstoque() + c.getQuantidadeUsada());
            insumosRepository.save(insumo);
        });
        consumoInsumoRepository.deleteAll(consumosAntigos);
        
        processarConsumos(request.consumos(), evolucao);

        EvolucaoEstetica salva = evolucaoEsteticaRepository.save(evolucao);
        return mapToResponse(salva);
    }

    @Override
    public AtendimentoResponse finalizarAtendimento(Long id) {
        EvolucaoEstetica evolucao = evolucaoEsteticaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado."));
        
        evolucao.setFinalizado(true);
        agendamentoService.mudarStatus(evolucao.getAgendamento().getId(), StatusAgendamento.CONCLUIDO);
        
        EvolucaoEstetica salva = evolucaoEsteticaRepository.save(evolucao);
        
        return mapToResponse(salva);
    }

    private void mapRequestToEntity(AtendimentoRequest request, EvolucaoEstetica evolucao) {
        evolucao.setRelatoClinico(request.relatoClinico());
        evolucao.setNotasProfissional(request.notasProfissional());
        evolucao.setObservacoes(request.observacoes());
        evolucao.setOrientacoesAoPaciente(request.orientacoesAoPaciente());
        evolucao.setIntercorrencias(request.intercorrencias());
        evolucao.setRetornoRecomendado(request.retornoRecomendado());
        evolucao.setNumeroSessao(request.numeroSessao());
        evolucao.setPesoPacienteKg(request.pesoPacienteKg());
        evolucao.setDoseAplicadaMg(request.doseAplicadaMg());

        if (request.procedimentosIds() != null) {
            evolucao.setProcedimentosRealizados(procedimentoRepository.findAllById(request.procedimentosIds()));
        }

        if (request.locaisIds() != null) {
            evolucao.setLocaisAplicados(localAplicacaoRepository.findAllById(request.locaisIds()));
        }
    }

    private void processarConsumos(List<InsumoRequestResponse> consumos, EvolucaoEstetica evolucao) {
        if (consumos != null && !consumos.isEmpty()) {
            consumos.forEach(c -> {
                Insumo insumo = insumosRepository.findById(c.id())
                        .orElseThrow(() -> new RegraNegocioException("Insumo não encontrado: " + c.id()));
                
                if (insumo.getQuantidadeEstoque() < c.quantidadeUsada()) {
                    // Opcional: Validar estoque
                }
                
                ConsumoInsumo consumoEntity = new ConsumoInsumo();
                consumoEntity.setEvolucao(evolucao);
                consumoEntity.setInsumo(insumo);
                consumoEntity.setQuantidadeUsada(c.quantidadeUsada());
                consumoEntity.setDataConsumo(LocalDateTime.now());
                
                consumoInsumoRepository.save(consumoEntity);
                
                insumo.setQuantidadeEstoque(insumo.getQuantidadeEstoque() - c.quantidadeUsada());
                insumosRepository.save(insumo);
            });
        }
    }

    private AtendimentoResponse mapToResponse(EvolucaoClinica evolucao) {
        AtendimentoResponse response = entityMapper.toAtendimentoResponse(evolucao);
        return populateConsumos(response, evolucao);
    }

    private AtendimentoResponse mapToResponse(EvolucaoEstetica evolucao) {
        AtendimentoResponse response = entityMapper.toAtendimentoResponse(evolucao);
        return populateConsumos(response, evolucao);
    }

    private AtendimentoResponse populateConsumos(AtendimentoResponse response, EvolucaoClinica evolucao) {
        List<ConsumoInsumo> consumos = consumoInsumoRepository.findByEvolucao(evolucao);
        List<InsumoRequestResponse> consumosDto = consumos.stream()
                .map(c -> new InsumoRequestResponse(
                        c.getInsumo().getId(),
                        c.getInsumo().getNome(),
                        c.getInsumo().getQuantidadeEstoque(),
                        c.getInsumo().getUnidadeMedida(),
                        c.getQuantidadeUsada()
                )).collect(Collectors.toList());
        
        // AtendimentoResponse é um record, precisamos criar um novo se quisermos mudar campos, 
        // ou garantir que o mapper já trate isso se possível. 
        // Como o record é imutável, vamos reconstruí-lo ou ajustar o mapper.
        
        return new AtendimentoResponse(
                response.id(),
                response.agendamentoId(),
                response.pacienteId(),
                response.pacienteNome(),
                response.profissionalId(),
                response.profissionalNome(),
                response.dataAtendimento(),
                response.statusAgendamento(),
                response.procedimentos(),
                response.relatoClinico(),
                response.notasProfissional(),
                response.observacoes(),
                response.orientacoesAoPaciente(),
                response.intercorrencias(),
                response.retornoRecomendado(),
                response.finalizado(),
                response.numeroSessao(),
                response.pesoPacienteKg(),
                response.doseAplicadaMg(),
                response.locaisAplicados(),
                consumosDto
        );
    }
}
