package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserApifyFilters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private UserApifyCallHistorial historial;
    private String hashtags;
    private String dateFrom;
    private String dateTo;
    private Long minLikes;
    private Long maxLikes;
    private Long NlastPostByHashtags = 1L;
    private String tiktokAccount;
}
