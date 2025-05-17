package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TiktokUsername {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @ManyToOne(optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private UserApifyCallHistorial historial;
}
