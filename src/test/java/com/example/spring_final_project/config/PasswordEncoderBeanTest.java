package com.example.spring_final_project.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderBeanTest {

    @Test
    void testPasswordEncoderBean_CreatesBCryptPasswordEncoder() {
        // Act
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
}