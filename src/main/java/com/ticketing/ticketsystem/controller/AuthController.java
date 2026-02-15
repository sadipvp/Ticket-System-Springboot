package com.ticketing.ticketsystem.controller;

import com.ticketing.ticketsystem.dto.LoginRequest; // Importamos el DTO
import com.ticketing.ticketsystem.models.User;
import com.ticketing.ticketsystem.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.ldap.simulation.users}")
    private String simulationUsersConfig;

    private Map<String, String> ldapSimulationMap = new HashMap<>();

    @PostConstruct
    public void init() {
        String[] users = simulationUsersConfig.split(",");
        for (String userPair : users) {
            String[] parts = userPair.split(":");
            if (parts.length == 2) {
                ldapSimulationMap.put(parts[0].trim(), parts[1].trim());
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Lógica LDAP (Simulada)
        String passwordCorrecta = ldapSimulationMap.get(request.username);
        if (passwordCorrecta == null)
            passwordCorrecta = "123";

        if (!request.password.equals(passwordCorrecta)) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas (LDAP)"));
        }

        // 2. Lógica Base de Datos
        User usuario = userRepository.findByUid(request.username).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(403).body(Map.of("message", "Usuario no existe en Base de Datos Local"));
        }

        return ResponseEntity.ok(usuario);
    }
}