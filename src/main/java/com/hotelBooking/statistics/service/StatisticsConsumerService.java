package com.hotelBooking.statistics.service;

import com.hotelBooking.statistics.model.RoomBookingEvent;
import com.hotelBooking.statistics.model.StatisticsEvent;
import com.hotelBooking.statistics.model.UserRegistrationEvent;
import com.hotelBooking.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsConsumerService {

    private final StatisticsRepository statisticsRepository;

    @KafkaListener(topics = "${app.kafka.topic.user-registration}", groupId = "hotel-booking-group")
    public void consumeUserRegistrationEvent(UserRegistrationEvent event) {
        log.info("Received USER_REGISTRATION event: {}", event);

        StatisticsEvent statsEvent = StatisticsEvent.builder()
                .eventType("USER_REGISTRATION")
                .userId(event.getUserId())
                .createdAt(LocalDateTime.now())
                .build();

        statisticsRepository.save(statsEvent);
        log.info("Saved USER_REGISTRATION statistics event for userId: {}", event.getUserId());
    }

    @KafkaListener(topics = "${app.kafka.topic.room-booking}", groupId = "hotel-booking-group")
    public void consumeRoomBookingEvent(RoomBookingEvent event) {
        log.info("Received ROOM_BOOKING event: {}", event);

        StatisticsEvent statsEvent = StatisticsEvent.builder()
                .eventType("ROOM_BOOKING")
                .userId(event.getUserId())
                .checkIn(event.getCheckIn())
                .checkOut(event.getCheckOut())
                .createdAt(LocalDateTime.now())
                .build();

        statisticsRepository.save(statsEvent);
        log.info("Saved ROOM_BOOKING statistics event for userId: {}", event.getUserId());
    }
}
