package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Procedimento;
import com.cljtech.clinica.data.repository.InsumosRepository;
import com.cljtech.clinica.data.repository.LocalAplicacaoRepository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import com.cljtech.clinica.service.ConfigsService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigsServiceImpl implements ConfigsService {

    private final LocalAplicacaoRepository localAplicacaoRepository;
    private final InsumosRepository insumosRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public void criarLocalAplicacao(LocalAplicacaoRequestResponse request) {
        localAplicacaoRepository.save(entityMapper.toLocalAplicacao(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalAplicacaoRequestResponse> listarLocaisAplicacao() {
        return entityMapper.toLocalAplicacaoRequest(localAplicacaoRepository.findAll());
    }

    @Override
    @Transactional
    public void criarInsumo(InsumoRequestResponse request) {
        insumosRepository.save(entityMapper.toInsumo(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoRequestResponse> listarInsumos() {
        return entityMapper.toInsumoRequestResponse(insumosRepository.findAll());
    }

    @Override
    @Transactional
    public void criarProcedimentos(ProcedimentoRequestResponse request) {
        validarPrecoProcedimento(request);
        procedimentoRepository.save(entityMapper.toProcedimento(request));
    }

    @Override
    @Transactional
    public ProcedimentoRequestResponse atualizarProcedimento(Long id, ProcedimentoRequestResponse request) {
        validarPrecoProcedimento(request);
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento não encontrado"));
        procedimento.setNome(request.nome());
        procedimento.setPrecoSugerido(request.precoSugerido());
        procedimento.setDuracaoMinutos(request.duracaoMinutos());
        return entityMapper.toProcedimentoRequestResponse(procedimentoRepository.save(procedimento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcedimentoRequestResponse> listarProcedimentos() {
        return entityMapper.toProcedimentoRequestResponse(procedimentoRepository.findAll());
    }

    private void validarPrecoProcedimento(ProcedimentoRequestResponse request) {
        if (request.precoSugerido() == null) {
            throw new RegraNegocioException("Preço do procedimento é obrigatório.");
        }
        if (request.precoSugerido().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Preço do procedimento não pode ser negativo.");
        }
    }
}
