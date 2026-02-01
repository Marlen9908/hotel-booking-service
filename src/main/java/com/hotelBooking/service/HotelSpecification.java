package com.hotelBooking.service;

import com.hotelBooking.model.Hotel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> filter(
            Long id,
            String name,
            String headline,
            String city,
            String address,
            Double minDistance,
            Double maxDistance,
            Double minRating,
            Double maxRating,
            Integer minNumberOfRatings,
            Integer maxNumberOfRatings
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (headline != null && !headline.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("headline")), "%" + headline.toLowerCase() + "%"));
            }
            if (city != null && !city.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (address != null && !address.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }
            if (minDistance != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("distanceFromCenter"), minDistance));
            }
            if (maxDistance != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("distanceFromCenter"), maxDistance));
            }
            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rating"), maxRating));
            }
            if (minNumberOfRatings != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("numberOfRatings"), minNumberOfRatings));
            }
            if (maxNumberOfRatings != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("numberOfRatings"), maxNumberOfRatings));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
