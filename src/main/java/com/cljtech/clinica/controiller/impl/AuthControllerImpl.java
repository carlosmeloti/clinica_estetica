package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.AuthController;
import com.cljtech.clinica.model.records.LoginRequest;
import com.cljtech.clinica.model.records.TokenResponse;
import com.cljtech.clinica.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
