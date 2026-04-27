package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.AuthController;
import com.cljtech.clinica.records.LoginRequest;
import com.cljtech.clinica.records.TokenResponse;
import com.cljtech.clinica.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest data) {
        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authManager.authenticate(authToken);

        var user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
