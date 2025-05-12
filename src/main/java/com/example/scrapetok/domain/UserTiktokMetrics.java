package com.example.scrapetok.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

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
    private Long postId;
    @Column(nullable = false)
    private Date datePosted;
    @Column(nullable = false)
    private Time hoursPosted;
    @Column(nullable = false)
    private String usernameTiktokAccount;
    @Column(nullable = false)
    private String postURL;
    @Column(nullable = false)
    private Long views;
    @Column(nullable = false)
    private Long likes;
    @Column(nullable = false)
    private Long comments;
    @Column(nullable = false)
    private Long saves;
    @Column(nullable = false)
    private Long reposts;
    @Column(nullable = false)
    private Long totalInteractions;
    @Column(nullable = false)
    private Double engagement;
    @Column(nullable = false)
    private Long numberHashtags;
    @Column(nullable = false)
    private String hashtags;
    @Column(nullable = false)
    private String soundURL;
    @Column(nullable = false)
    private String regionPost;
    @Column(nullable = false)
    private Date dateTracking;
    @Column(nullable = false)
    private Time timeTracking;
}
