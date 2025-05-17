package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccount extends GeneralAccount {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
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
    private List<DailyAlerts> alert= new ArrayList<>();;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    List<AdminTiktokMetrics> TiktokMetrics = new ArrayList<>();
}
