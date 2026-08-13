package com.hanson.plusone.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "display_name", nullable = false, length = 60)
    private String displayName;

    @Column(length = 500)
    private String bio;

    @Column(length = 100)
    private String city;

    @Column(name = "connection_goal", length = 200)
    private String connectionGoal;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected UserProfile() {
    }

    public UserProfile(
            User user,
            String displayName,
            String bio,
            String city,
            String connectionGoal) {
        this.user = user;
        this.displayName = displayName;
        this.bio = bio;
        this.city = city;
        this.connectionGoal = connectionGoal;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getCity() {
        return city;
    }

    public String getConnectionGoal() {
        return connectionGoal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void update(
        String displayName,
        String bio,
        String city,
        String connectionGoal
    ){
        this.displayName = displayName;
        this.bio = bio;
        this.city = city;
        this.connectionGoal = connectionGoal;
    }
}