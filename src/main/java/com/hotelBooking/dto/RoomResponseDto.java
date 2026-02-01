package com.hotelBooking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer roomNumber;
    private Double price;
    private Integer maxGuests;
    private Long hotelId;
    private String hotelName;
}
