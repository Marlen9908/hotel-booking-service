package com.hotelBooking.service;

import com.hotelBooking.dto.BookingRequestDto;
import com.hotelBooking.dto.BookingResponseDto;
import com.hotelBooking.model.Booking;
import com.hotelBooking.model.Room;
import com.hotelBooking.model.User;
import com.hotelBooking.repository.BookingRepository;
import com.hotelBooking.repository.RoomRepository;
import com.hotelBooking.repository.UserRepository;
import com.hotelBooking.statistics.model.RoomBookingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final KafkaTemplate<String, RoomBookingEvent> kafkaTemplate;

    @Value("${app.kafka.topic.room-booking}")
    private String roomBookingTopic;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + dto.getRoomId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        // Check date conflicts: room must be free for the requested dates
        // A conflict exists if there's a booking that overlaps: existingCheckIn < requestedCheckOut AND existingCheckOut > requestedCheckIn
        List<Booking> conflictingBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getRoom().getId().equals(dto.getRoomId()))
                .filter(b -> b.getCheckIn().isBefore(dto.getCheckOut()) && b.getCheckOut().isAfter(dto.getCheckIn()))
                .collect(Collectors.toList());

        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("Room is not available for the requested dates");
        }

        Booking booking = Booking.builder()
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .room(room)
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);

        // Send Kafka event
        RoomBookingEvent event = RoomBookingEvent.builder()
                .userId(user.getId())
                .checkIn(dto.getCheckIn().toString())
                .checkOut(dto.getCheckOut().toString())
                .build();
        kafkaTemplate.send(roomBookingTopic, String.valueOf(user.getId()), event);

        return bookingMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getAllBookings(Pageable pageable) {
        Page<Booking> page = bookingRepository.findAll(pageable);
        return page.map(bookingMapper::toResponseDto);
    }
}
