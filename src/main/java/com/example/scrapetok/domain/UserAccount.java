package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccount extends GeneralAccount {
    @Column(nullable = false)
    private String username;
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserApifyCallHistorial historial;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)  // Relación Débil
    private List<UserTiktokMetrics> userTiktokMetrics = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestAndAnswer> questions = new ArrayList<>();
}
