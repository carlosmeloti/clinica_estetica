package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.AuthController;
import com.cljtech.clinica.records.LoginRequest;
import com.cljtech.clinica.records.TokenResponse;
import com.cljtech.clinica.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    @Override
    public ResponseEntity<String> login(LoginRequest data) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = authManager.authenticate(authToken);

            // No Spring 7, é mais seguro extrair o UserDetails assim:
            var user = (UserDetails) auth.getPrincipal();
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new TokenResponse(token).token());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
