package com.cljtech.clinica.controller;

import com.cljtech.clinica.model.records.ProfissionalResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public interface ProfissionalController {

    @PostMapping
    ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioRequest request);

    @GetMapping
    ResponseEntity<List<ProfissionalResponse>> listar();

    @GetMapping("/{id}")
    ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id);
}
