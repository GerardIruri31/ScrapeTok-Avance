package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class DailyAlerts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminAccount admin;


    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String body;
    @Column(nullable = false)
    private Date postedDate;
    @Column(nullable = false)
    private Time postedTime;
    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL)
    private List<Email> emails = new ArrayList<>();
}
