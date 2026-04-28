package com.cljtech.clinica.controiller;


import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public interface UsuarioController {

    @PostMapping("/criar")
    ResponseEntity<Void> criar(@RequestBody UsuarioRequest usuario);

    @GetMapping("/listar")
    ResponseEntity<List<UsuarioResponse>> listar();

    @GetMapping("/{login}")
    ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login);
}
