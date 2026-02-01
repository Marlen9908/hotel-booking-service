package com.hotelBooking.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Long id;
    private Long roomId;
    private String roomName;
    private Long userId;
    private String username;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
