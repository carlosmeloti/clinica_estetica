package com.cljtech.clinica.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class LocalAplicacao extends EntidadeBase {
    @Column(nullable = false, unique = true)
    private String nome; // Ex: "Abdômen", "Coxa", "Rosto"
    private boolean preDefinido = false;

}
