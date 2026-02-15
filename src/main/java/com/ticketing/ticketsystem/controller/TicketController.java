package com.ticketing.ticketsystem.controller;

import com.ticketing.ticketsystem.models.Ticket;
import com.ticketing.ticketsystem.models.User;
import com.ticketing.ticketsystem.Repository.TicketRepository;
import com.ticketing.ticketsystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    // Estructura de datos que recibimos del Frontend
    public static class TicketRequest {
        public String title;
        public String description;
        public String priority; // BAJA, MEDIA, ALTA
        public String category; // SOPORTE, HARDWARE, ETC
        public String creatorUid;
    }

    // 1. CREAR TICKET + ASIGNACIÓN AUTOMÁTICA
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest request) {

        User creator = userRepository.findByUid(request.creatorUid).orElse(null);
        if (creator == null)
            return ResponseEntity.badRequest().body("Usuario no encontrado");

        // --- ALGORITMO DE ASIGNACIÓN (LOAD BALANCING) ---
        List<User> agents = userRepository.findByRole("AGENT"); // Asegúrate de tener este método en UserRepository
        User bestAgent = null;
        long minLoad = Long.MAX_VALUE;

        // Buscamos al agente con menos trabajo
        for (User agent : agents) {
            long load = ticketRepository.countActiveTickets(agent.getId());
            if (load < minLoad) {
                minLoad = load;
                bestAgent = agent;
            }
        }

        Ticket ticket = new Ticket(request.title, request.description, request.priority, request.category, creator);

        if (bestAgent != null) {
            ticket.setAssignedTo(bestAgent);
            ticket.setStatus("ASIGNADO"); // O "ABIERTO"
            System.out.println("Ticket asignado a: " + bestAgent.getName());
        } else {
            ticket.setStatus("PENDIENTE");
        }

        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }

    // 2. LISTAR TICKETS SEGÚN EL ROL
    @GetMapping("/list")
    public ResponseEntity<?> listTickets(@RequestParam String uid) {
        User user = userRepository.findByUid(uid).orElse(null);
        if (user == null)
            return ResponseEntity.badRequest().build();

        List<Ticket> tickets;

        if ("ADMIN".equals(user.getRole())) {
            tickets = ticketRepository.findAll(); // Admin ve todo
        } else if ("AGENT".equals(user.getRole())) {
            tickets = ticketRepository.findByAssignedToUid(uid); // Agente ve lo suyo
        } else {
            tickets = ticketRepository.findByCreatedByUid(uid); // Usuario ve lo que creó
        }

        return ResponseEntity.ok(tickets);
    }

    // 3. ACTUALIZAR ESTADO (Para Agentes/Admin)
    @PostMapping("/{id}/update-status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null)
            return ResponseEntity.notFound().build();

        String newStatus = body.get("status");
        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        return ResponseEntity.ok(ticket);
    }
}