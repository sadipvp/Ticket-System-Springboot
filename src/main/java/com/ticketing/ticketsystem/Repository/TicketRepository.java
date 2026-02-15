package com.ticketing.ticketsystem.Repository;

import com.ticketing.ticketsystem.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // USUARIO: Ver los que ÉL creó
    List<Ticket> findByCreatedByUid(String uid);

    // AGENTE: Ver los que le asignaron a ÉL
    List<Ticket> findByAssignedToUid(String uid);

    // LÓGICA DE ASIGNACIÓN (Load Balancer)
    // Cuenta cuántos tickets "ABIERTO" o "EN PROGRESO" tiene cada agente
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo.id = :agentId AND t.status NOT IN ('RESUELTO', 'CERRADO')")
    long countActiveTickets(Long agentId);
}