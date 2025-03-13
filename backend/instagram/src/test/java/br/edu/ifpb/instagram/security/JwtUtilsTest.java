package br.edu.ifpb.instagram.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String SECRET_KEY = "umaChaveMuitoSeguraDePeloMenos64CaracteresParaHS512JwtAlgoritmo";


    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        if (jwtUtils==null)jwtUtils = new JwtUtils();
    }

    /*
    Verifica se ValidadeToken está validando corretamente um token valido
     */
    @Test
    void testValidateToken_ValidToken() {
        String token = generateValidToken("testUser");
        assertTrue(jwtUtils.validateToken(token));
    }

    /*
Verifica se ValidadeToken está invalidando um token invalido
 */
    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "TokenQualquerInvalido";
        assertFalse(jwtUtils.validateToken(invalidToken));
    }

    /*
    Verifica swe generateToken não gera token null e se gera token valido
     */
    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("testUser");
        String token = jwtUtils.generateToken(authentication);
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));
    }


/*
Passa um Token valido como parametro para o metodo
getUsernameFromToken e verifica se ele retorna o mesmo userName passado como parametro
 */
    @Test
    void testGetUsernameFromToken() {
        String token = generateValidToken("testUser");
        String username = jwtUtils.getUsernameFromToken(token);
        assertEquals("testUser", username);
    }

    /*
    Metodo auxiliar para gerar token valido
     */
    private String generateValidToken(String username) {
        var jwtSecret = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        var jwtExpirationMs = 86400000;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret), SignatureAlgorithm.HS512)
                .compact();
    }
}