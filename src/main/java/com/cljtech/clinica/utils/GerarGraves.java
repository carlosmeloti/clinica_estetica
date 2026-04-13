//package com.cljtech.clinica.utils;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class GerarGraves {
//    public static void main(String[] args) throws Exception {
//        // Gera uma chave segura para o algoritmo HMAC-SHA256
//        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//        keyGen.init(256);
//        SecretKey secretKey = keyGen.generateKey();
//
//        // Converte para Base64 para você copiar e colar no seu projeto
//        String chaveBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//
//        System.out.println("Sua SECRET_KEY segura:");
//        System.out.println(chaveBase64);
//    }
//}
