package com.hanson.plusone.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User host;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "max_attendees", nullable = false)
    private Integer maxAttendees;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Event() {
    }

    public Event(
            User host,
            String title,
            String description,
            String location,
            String category,
            LocalDateTime startsAt,
            Integer maxAttendees) {
        this.host = host;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.startsAt = startsAt;
        this.maxAttendees = maxAttendees;
    }

    public Long getId() {
        return id;
    }

    public User getHost() {
        return host;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void update(
        String title,
        String description,
        String location,
        String category,
        LocalDateTime startsAt,
        Integer maxAttendees) {

    this.title = title;
    this.description = description;
    this.location = location;
    this.category = category;
    this.startsAt = startsAt;
    this.maxAttendees = maxAttendees;
    }
}