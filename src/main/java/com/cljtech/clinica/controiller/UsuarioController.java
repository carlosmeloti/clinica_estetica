package com.cljtech.clinica.controiller;


import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.records.UsuarioRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/usuarios")
public interface UsuarioController {

    @PostMapping("/criar")
    ResponseEntity<Void> criar(@RequestBody UsuarioRequest usuario);

    @GetMapping
    ResponseEntity<List<Usuario>> listar();

    @GetMapping("/{id}")
    ResponseEntity<Usuario> buscarPorLogin(@PathVariable String login);
}
