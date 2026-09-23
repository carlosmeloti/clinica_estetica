package com.cljtech.clinica.controller;

import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configs")
public interface ConfigsController {

    @PostMapping("/locaisaplicacao/criar")
    ResponseEntity<Void> criarLocaisAplicacao(@RequestBody LocalAplicacaoRequestResponse request);

    @GetMapping("/locaisaplicacao/listar")
    ResponseEntity <List<LocalAplicacaoRequestResponse>> listarLocaisAplicacao();

    @PostMapping("/insumos/criar")
    ResponseEntity<Void> criarInsumo(@RequestBody InsumoRequestResponse request);

    @GetMapping("/insumos/listar")
    ResponseEntity <List<InsumoRequestResponse>> listarInsumos();

    @PostMapping("/procedimentos/criar")
    ResponseEntity<Void> criarProcedimentos(@RequestBody @Valid ProcedimentoRequestResponse request);

    @PutMapping("/procedimentos/atualizar/{id}")
    ResponseEntity<ProcedimentoRequestResponse> atualizarProcedimento(
            @PathVariable Long id,
            @RequestBody @Valid ProcedimentoRequestResponse request
    );

    @GetMapping("/procedimentos/listar")
    ResponseEntity <List<ProcedimentoRequestResponse>> listarProcedimentos();


}
