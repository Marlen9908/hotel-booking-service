package com.hotelBooking.statistics.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationEvent {
    private Long userId;
}
