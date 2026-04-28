package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.records.LoginRequest;
import com.cljtech.clinica.model.records.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request);
}
