package com.hotelBooking.service;

import com.hotelBooking.dto.BookingResponseDto;
import com.hotelBooking.model.Booking;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
public abstract class BookingMapper {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.name", target = "roomName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    public abstract BookingResponseDto toResponseDto(Booking booking);
}
