package com.cljtech.clinica.controiller;

import com.cljtech.clinica.records.LocalAplicacaoRequest;
import com.cljtech.clinica.records.UsuarioRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configs")
public interface ConfigsController {

    @PostMapping("/locaisaplicacao")
    ResponseEntity<Void> criarLocaisAplicacao(@RequestBody LocalAplicacaoRequest request);
}
