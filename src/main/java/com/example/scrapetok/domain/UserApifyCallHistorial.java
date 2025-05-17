package com.example.scrapetok.domain;

import com.example.scrapetok.domain.enums.ApifyRunStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserApifyCallHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount userAccount;


    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Integer amountScrappedAccount;

    @OneToMany(mappedBy = "historial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserApifyFilters> filtros= new ArrayList<>();;

    @OneToMany(mappedBy = "historial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TiktokUsername> usernames = new ArrayList<>();;
}
