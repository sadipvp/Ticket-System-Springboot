package com.ticketing.ticketsystem.config;

import com.ticketing.ticketsystem.models.User;
import com.ticketing.ticketsystem.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            System.out.println("----------------------------------------------------------------");
            System.out.println(">>> Iniciando carga de usuarios iniciales...");

            // 1. Crear ADMIN (Juan Perez)
            if (repository.findByUid("jperez").isEmpty()) {
                repository.save(new User("jperez", "Juan Perez", "juan@test.com", "ADMIN"));
                System.out.println(">>> [OK] Usuario 'jperez' (ADMIN) creado.");
            }

            // 2. Crear AGENTE (Maria Garcia)
            if (repository.findByUid("mgarcia").isEmpty()) {
                repository.save(new User("mgarcia", "Maria Garcia", "maria@agent.com", "AGENT"));
                System.out.println(">>> [OK] Usuario 'mgarcia' (AGENT) creado.");
            }

            // 3. Crear USUARIO FINAL (Carlos Lopez)
            if (repository.findByUid("clopez").isEmpty()) {
                repository.save(new User("clopez", "Carlos Lopez", "carlos@user.com", "USER"));
                System.out.println(">>> [OK] Usuario 'clopez' (USER) creado.");
            }

            System.out.println(">>> Carga de datos finalizada.");
            System.out.println("----------------------------------------------------------------");
        };
    }
}