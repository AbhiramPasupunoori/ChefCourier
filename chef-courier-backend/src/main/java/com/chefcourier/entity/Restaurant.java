package com.chefcourier.entity;

import com.chefcourier.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 80)
    private String cuisineType;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 250)
    private String address;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RestaurantStatus status =
            RestaurantStatus.PENDING;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private double averageRating = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void beforeInsert() {
        createdAt = LocalDateTime.now();
    }
}
