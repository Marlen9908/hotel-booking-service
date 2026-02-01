package com.hotelBooking.service;

import com.hotelBooking.model.Booking;
import com.hotelBooking.model.Room;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static Specification<Room> filter(
            Long id,
            String name,
            Double minPrice,
            Double maxPrice,
            Integer minGuests,
            Integer maxGuests,
            LocalDate checkIn,
            LocalDate checkOut,
            Long hotelId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (minGuests != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxGuests"), minGuests));
            }
            if (maxGuests != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxGuests"), maxGuests));
            }
            if (hotelId != null) {
                predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            }

            // Date filtering: both checkIn and checkOut must be provided
            // Show only rooms that are FREE in the requested range
            // A room is busy if there is a booking where: booking.checkIn < requestedCheckOut AND booking.checkOut > requestedCheckIn
            if (checkIn != null && checkOut != null) {
                // Subquery to find room IDs that have conflicting bookings
                Subquery<Long> busyRoomIds = query.subquery(Long.class);
                Root<Booking> bookingRoot = busyRoomIds.from(Booking.class);
                busyRoomIds.select(bookingRoot.get("room").get("id"));
                busyRoomIds.where(
                        cb.and(
                                cb.lessThan(bookingRoot.get("checkIn"), checkOut),
                                cb.greaterThan(bookingRoot.get("checkOut"), checkIn)
                        )
                );

                // Exclude rooms that are busy
                predicates.add(cb.not(root.get("id").in(busyRoomIds)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
