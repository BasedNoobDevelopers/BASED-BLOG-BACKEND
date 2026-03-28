package com.noobsmoke.basedblogbackend.unit.service;

import com.noobsmoke.basedblogbackend.TestUtils;
import com.noobsmoke.basedblogbackend.model.User;
import com.noobsmoke.basedblogbackend.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest extends TestUtils {

    private JWTService underTest;

    @BeforeEach
    void setUp() {
        underTest = new JWTService();

        ReflectionTestUtils.setField(
                underTest,
                "secretKey",
                fakeSecretKey
        );

        ReflectionTestUtils.setField(
                underTest,
                "jwtExpirationTime",
                fakeExpirationTime
        );
    }

    @Test
    void shouldGenerateTokenAndExtractUsername() {
        User user = getUsers().getFirst();
        String token = underTest.generateToken(user);
        String extractUsername = underTest.extractUsername(token);
        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(user.getUsername(), extractUsername);
    }

    @Test
    void shouldReturnTrueWhenTokenIsValidForUser() {
        User user = getUsers().getFirst();
        String token = underTest.generateToken(user);
        assertTrue(underTest.isTokenValid(token, user));
    }

    @Test
    void shouldReturnFalseWhenTokenDoesNotMatchUser() {
        User user = getUsers().getFirst();
        User falseUser = getUsers().getLast();
        String token = underTest.generateToken(user);
        assertFalse(underTest.isTokenValid(token, falseUser));
    }
}
