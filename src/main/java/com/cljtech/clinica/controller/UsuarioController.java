package com.cljtech.clinica.controller;


import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public interface UsuarioController {

    @PostMapping
    ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioRequest usuario);

    @PutMapping("/{id}")
    ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequest usuario);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long id);

    @GetMapping("/{id}")
    ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Page<UsuarioResponse>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) PerfilUsuario perfil,
            Pageable pageable);
}
