package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.repository.InsumosRepository;
import com.cljtech.clinica.data.repository.LocalAplicacaoRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.service.ConfigsService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigsServiceImpl implements ConfigsService {

    private final LocalAplicacaoRepository localAplicacaoRepository;
    private final InsumosRepository insumosRepository;
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
}
