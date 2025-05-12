package com.example.scrapetok.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String userEmail;
    @ManyToOne(optional = false)
    @JoinColumn(name = "alert_id",nullable = false)
    private DailyAlerts alert;
}
