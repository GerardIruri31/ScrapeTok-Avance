package com.example.scrapetok.domain;

import com.example.scrapetok.domain.enums.statusQA;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
public class QuestAndAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;


    @ManyToOne
    @JoinColumn(name = "admin_id") // Puede ser null si pregunta aún no se responde
    private AdminAccount admin;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private statusQA status = statusQA.PENDING;
    @Column(nullable = false)
    private String questionDescription;
    @Column(nullable = false)
    private LocalDate questionDate;
    @Column(nullable = false)
    private LocalTime questionHour;
    private String answerDescription;
    private LocalDate answerDate;
    private LocalTime  answerHour;
}
