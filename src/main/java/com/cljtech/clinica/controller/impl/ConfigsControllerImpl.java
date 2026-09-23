package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.ConfigsController;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import com.cljtech.clinica.service.ConfigsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfigsControllerImpl implements ConfigsController {

    public final ConfigsService configsService;

    @Override
    public ResponseEntity<Void> criarLocaisAplicacao(LocalAplicacaoRequestResponse request) {
        configsService.criarLocalAplicacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<LocalAplicacaoRequestResponse>> listarLocaisAplicacao() {
        return ResponseEntity.ok(configsService.listarLocaisAplicacao());
    }

    @Override
    public ResponseEntity<Void> criarInsumo(InsumoRequestResponse request) {
        configsService.criarInsumo(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<InsumoRequestResponse>> listarInsumos() {
        return ResponseEntity.ok(configsService.listarInsumos());
    }

    @Override
    public ResponseEntity<Void> criarProcedimentos(@Valid ProcedimentoRequestResponse request) {
        configsService.criarProcedimentos(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<ProcedimentoRequestResponse> atualizarProcedimento(
            Long id,
            @Valid ProcedimentoRequestResponse request
    ) {
        return ResponseEntity.ok(configsService.atualizarProcedimento(id, request));
    }

    @Override
    public ResponseEntity<List<ProcedimentoRequestResponse>> listarProcedimentos() {
        return ResponseEntity.ok(configsService.listarProcedimentos());
    }


}
