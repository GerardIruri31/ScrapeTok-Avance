package com.example.scrapetok.domain;

import com.example.scrapetok.domain.enums.ApifyRunStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserApifyFilters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private UserApifyCallHistorial historial;

    @Column(columnDefinition = "TEXT")
    private String hashtags;
    private String dateFrom;
    private String dateTo;
    private Integer minLikes;
    private Integer maxLikes;
    private Integer NlastPostByHashtags = 1;
    @Column(columnDefinition = "TEXT")
    private String tiktokAccount;

    @Enumerated(EnumType.STRING)
    private ApifyRunStatus apifyRunStatus = ApifyRunStatus.FAILED;
    private Integer executionTime;
}
