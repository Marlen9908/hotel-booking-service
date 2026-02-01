package com.hotelBooking.statistics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsEvent {

    @Id
    private String id;

    private String eventType; // "USER_REGISTRATION" or "ROOM_BOOKING"

    private Long userId;

    // For booking events
    private String checkIn;
    private String checkOut;

    private LocalDateTime createdAt;
}
