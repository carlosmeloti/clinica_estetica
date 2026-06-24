package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.model.records.LoginRequest;
import com.cljtech.clinica.model.records.TokenResponse;
import com.cljtech.clinica.security.JwtService;
import com.cljtech.clinica.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public TokenResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var auth = authManager.authenticate(authToken);

        var user = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(user);

    }

}
