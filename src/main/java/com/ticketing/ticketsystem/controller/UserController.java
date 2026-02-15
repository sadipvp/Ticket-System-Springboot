package com.ticketing.ticketsystem.controller;

import com.ticketing.ticketsystem.dto.RegisterRequest; // Importamos el DTO
import com.ticketing.ticketsystem.models.User;
import com.ticketing.ticketsystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users") // Fíjate que la ruta cambia, ahora es /api/users
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // 1. Validar si existe
        if (userRepository.findByUid(request.username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El usuario ya existe"));
        }

        // 2. Crear
        User newUser = new User(
                request.username,
                request.name,
                request.email,
                "USER" // Por defecto entra como usuario normal
        );

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente"));
    }
}