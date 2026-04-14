package com.cljtech.clinica.controiller;

import com.cljtech.clinica.records.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    public ResponseEntity<String> login(@RequestBody LoginRequest request);
}
