package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class TiktokUsername {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;

    @ManyToMany(mappedBy = "tiktokUsernames")
    private Set<UserApifyCallHistorial> historial = new HashSet<>();
}
