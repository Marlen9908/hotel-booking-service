package com.hotelBooking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponseDto {
    private Long id;
    private String name;
    private String headline;
    private String city;
    private String address;
    private Double distanceFromCenter;
    private Double rating;
    private Integer numberOfRatings;
}
