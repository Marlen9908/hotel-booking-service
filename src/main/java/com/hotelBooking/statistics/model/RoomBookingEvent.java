package com.hotelBooking.statistics.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBookingEvent {
    private Long userId;
    private String checkIn;
    private String checkOut;
}
