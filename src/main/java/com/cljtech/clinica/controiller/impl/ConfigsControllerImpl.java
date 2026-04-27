package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.ConfigsController;
import com.cljtech.clinica.data.repository.LocalAplicacaoRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.records.LocalAplicacaoRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfigsControllerImpl implements ConfigsController {

    public final LocalAplicacaoRepository localAplicacaoRepository;
    public final EntityMapper entityMapper;

    @Override
    public ResponseEntity<Void> criarLocaisAplicacao(LocalAplicacaoRequestResponse request) {
        localAplicacaoRepository.save(entityMapper.toLocalAplicacao(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<LocalAplicacaoRequestResponse>> listarLocaisAplicacao() {
        return ResponseEntity.ok(localAplicacaoRepository.findAll().stream()
                .map(entityMapper::toLocalAplicacaoRequest)
                .toList());
    }
}
