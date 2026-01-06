package com.beerreal.beerreal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "beer_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "beer_name")
    private String beerName;

    @Column(name = "location")
    private String location;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "year")
    private Integer year;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        year = createdAt.getYear();
    }

}
