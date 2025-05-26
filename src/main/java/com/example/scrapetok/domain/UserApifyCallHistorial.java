package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "user")

@Data
public class UserApifyCallHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private GeneralAccount user;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Integer amountScrappedAccount;

    @OneToMany(mappedBy = "historial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserApifyFilters> filtros= new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "historial_TTUsername",
            // Dueño de la relación
            joinColumns = @JoinColumn(name = "historial_id"),
            inverseJoinColumns = @JoinColumn(name = "tiktok_username_id")
    )

    private Set<TiktokUsername> tiktokUsernames = new HashSet<>();
}
