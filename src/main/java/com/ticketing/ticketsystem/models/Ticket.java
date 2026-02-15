package com.ticketing.ticketsystem.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // En el ER es 'title'

    @Column(columnDefinition = "TEXT")
    private String description; // En el ER es 'text'

    private String priority; // ENUM: BAJA, MEDIA, ALTA
    private String category; // Ej: "Hardware", "Software" (Simulando category_id)
    private String status; // Ej: "ABIERTO", "CERRADO" (Simulando status_id)

    // RELACIONES (FKs del ER)
    @ManyToOne
    @JoinColumn(name = "created_by") // En el ER: created_by
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to") // En el ER: assigned_to
    private User assignedTo;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Ticket() {
    }

    public Ticket(String title, String description, String priority, String category, User createdBy) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.createdBy = createdBy;
        this.status = "ABIERTO"; // Valor por defecto
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCategory() {
        return category;
    }
}