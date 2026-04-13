package com.cljtech.clinica.controiller;


import com.cljtech.clinica.data.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/admin/usuarios")
public interface UsuarioController {

    @PostMapping
    ResponseEntity<Usuario> criar(@RequestBody Usuario usuario);

    @GetMapping
    ResponseEntity<List<Usuario>> listar();

    @GetMapping("/{id}")
    ResponseEntity<Usuario> buscarPorLogin(@PathVariable String login);
}
