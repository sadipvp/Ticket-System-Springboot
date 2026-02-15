package com.ticketing.ticketsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestConexion implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public TestConexion(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("\n==============================");
            System.out.println("🔥 ¡CONECTADO A POSTGRESQL! 🔥");
            System.out.println("==============================\n");
        } catch (Exception e) {
            System.err.println("\n==============================");
            System.err.println("❌ ERROR DE CONEXIÓN: " + e.getMessage());
            System.err.println("==============================\n");
        }
    }
}