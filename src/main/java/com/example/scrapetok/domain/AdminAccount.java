package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
    private Date admisionToAdminDate;
    @Column(nullable = false)
    private Time admisionToAdminTime;
    @Column(nullable = false)
    private Long totalQuestionsAnswered;
    @Column(nullable = false)
    private Boolean isActive = true;


    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<QuestAndAnswer> answers = new ArrayList<>();


    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<DailyAlerts> alert= new ArrayList<>();;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    List<AdminTiktokMetrics> TiktokMetrics = new ArrayList<>();
}
