package com.cljtech.clinica.data.tests;

import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final EmailService emailService;
    // O Spring resolve e injeta a dependência automaticamente
    public PedidoService(EmailService emailService) {
        this.emailService = emailService;
    }
    public void processar() {
        emailService.enviar("Pedido processado!");
    }
}
