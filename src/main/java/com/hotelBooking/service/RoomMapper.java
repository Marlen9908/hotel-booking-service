package com.hotelBooking.service;

import com.hotelBooking.dto.RoomRequestDto;
import com.hotelBooking.dto.RoomResponseDto;
import com.hotelBooking.model.Room;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
public abstract class RoomMapper {

    // DTO request -> Room entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)   // set manually in service
    @Mapping(target = "bookings", ignore = true)
    public abstract Room toEntity(RoomRequestDto dto);

    // Update existing entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    public abstract void updateEntity(RoomRequestDto dto, @MappingTarget Room room);

    // Room entity -> response DTO
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    public abstract RoomResponseDto toResponseDto(Room room);
}
