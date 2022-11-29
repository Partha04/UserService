package com.cloud.userservice.util;

import com.cloud.userservice.testUtils.PostgresTestContainer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTServiceTest extends PostgresTestContainer {
    @Autowired
    JWTService jwtService;

    @Test
    public void create_and_decode_of_valid_JWT_successfull() {
        String jwtId = UUID.randomUUID().toString();
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "subject";
        int jwtTimeToLive = 800000;
        String jwt = jwtService.createJWT(
                jwtId,
                jwtIssuer,
                jwtSubject,
                jwtTimeToLive
        );
        Claims claims = jwtService.decodeJWT(jwt);
        assertEquals(jwtId, claims.getId());
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtSubject, claims.getSubject());

    }  @Test
    public void create_and_decode_of_valid_JWT_without_expiry_successfull() {
        String jwtId = UUID.randomUUID().toString();
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "subject";
        int jwtTimeToLive = 0;
        String jwt = jwtService.createJWT(
                jwtId,
                jwtIssuer,
                jwtSubject,
                jwtTimeToLive
        );
        Claims claims = jwtService.decodeJWT(jwt);
        assertEquals(jwtId, claims.getId());
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtSubject, claims.getSubject());

    }


    @Test
    public void decode_should_fail_for_invalid_jwt() {
        String notAJwt = "This is not a JWT";
        assertThrows(MalformedJwtException.class, () -> jwtService.decodeJWT(notAJwt));
    }

    @Test
    public void decode_should_fail_for_null_jwt() {
        String notAJwt = null;
        assertThrows(IllegalArgumentException.class, () -> jwtService.decodeJWT(notAJwt));
    }

    @Test
    public void decode_gives_error_for_tampered_jwt() {
        String jwtId = "SOMEID1234";
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "Andrew";
        int jwtTimeToLive = 800000;
        String jwt = jwtService.createJWT(
                jwtId,
                jwtIssuer,
                jwtSubject,
                jwtTimeToLive
        );
        // tamper with the JWT
        StringBuilder tamperedJwt = new StringBuilder(jwt);
        tamperedJwt.setCharAt(22, 'I');
        assertNotEquals(jwt, tamperedJwt);
        assertThrows(SignatureException.class, () -> jwtService.decodeJWT(tamperedJwt.toString()));
    }
}