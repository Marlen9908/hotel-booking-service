package com.hotelBooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Связь с бронированиями для определения занятых дат
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private Set<Booking> bookings = new HashSet<>();
}
