package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity

public class UserTiktokMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;
    @Column(nullable = false)
    private String postId;
    @Column(nullable = false)
    private LocalDate datePosted;
    @Column(nullable = false)
    private LocalTime  hourPosted;
    @Column(nullable = false)
    private String usernameTiktokAccount;
    @Column(nullable = false)
    private String postURL;
    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false)
    private Integer likes;
    @Column(nullable = false)
    private Integer comments;
    @Column(nullable = false)
    private Integer saves;
    @Column(nullable = false)
    private Integer reposts;
    @Column(nullable = false)
    private Integer totalInteractions;
    @Column(nullable = false)
    private Double engagement;
    @Column(nullable = false)
    private Integer numberHashtags;
    @Column(nullable = false)
    private String hashtags;
    @Column(nullable = false)
    private String soundId;
    @Column(nullable = false)
    private String soundURL;
    @Column(nullable = false)
    private String regionPost;
    @Column(nullable = false)
    private LocalDate dateTracking;
    @Column(nullable = false)
    private LocalTime timeTracking;
}
