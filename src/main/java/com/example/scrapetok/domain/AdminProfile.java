package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class AdminProfile {
    @Id
    private Long id;
    @OneToOne
    @MapsId // indica que usa la misma clave que GeneralAccount
    @JoinColumn(name = "id")
    private GeneralAccount user;


    @Column(nullable = false)
    private LocalDate admisionToAdminDate;
    @Column(nullable = false)
    private LocalTime admisionToAdminTime;
    @Column(nullable = false)
    private Integer totalQuestionsAnswered;
    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<QuestAndAnswer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<DailyAlerts> alert= new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    List<AdminTiktokMetrics> TiktokMetrics = new ArrayList<>();
}
