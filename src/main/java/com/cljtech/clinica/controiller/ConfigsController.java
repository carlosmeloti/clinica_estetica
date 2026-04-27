package com.cljtech.clinica.controiller;

import com.cljtech.clinica.records.InsumoRequestResponse;
import com.cljtech.clinica.records.LocalAplicacaoRequestResponse;
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


}
