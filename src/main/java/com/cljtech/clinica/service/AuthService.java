package com.cljtech.clinica.service;

import com.cljtech.clinica.model.records.LoginRequest;
import com.cljtech.clinica.model.records.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);
}
