package com.cljtech.clinica.service;

import com.cljtech.clinica.records.LoginRequest;
import com.cljtech.clinica.records.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);
}
