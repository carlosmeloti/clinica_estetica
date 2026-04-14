package com.cljtech.clinica.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 2. Verifica se a data de expiração já passou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 3. Extrai a data de expiração do corpo (Claims)
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 4. Método genérico para extrair qualquer Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 5. Faz o "parse" do token usando a chave secreta
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Mudou de setSigningKey para verifyWith
                .build()
                .parseSignedClaims(token)   // Mudou de parseClaimsJws
                .getPayload();              // Mudou de getBody
    }


}
