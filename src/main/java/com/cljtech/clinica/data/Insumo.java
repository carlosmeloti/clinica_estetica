package com.cljtech.clinica.data;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Insumo extends EntidadeBase{
    private String nome; // Ex: "Cera de Mel", "Cera de Algas"
    private Double quantidadeEstoque; // em gramas ou kg
    private String unidadeMedida; // "g", "kg", "ml"

}
