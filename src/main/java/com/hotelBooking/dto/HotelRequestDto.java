package com.hotelBooking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequestDto {
    private String name;
    private String headline;
    private String city;
    private String address;
    private Double distanceFromCenter;
    // rating and numberOfRatings are NOT included - they cannot be changed via HTTP
}
