package com.hotelBooking.service;

import com.hotelBooking.dto.HotelRequestDto;
import com.hotelBooking.dto.HotelResponseDto;
import com.hotelBooking.model.Hotel;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;


@Component
public abstract class HotelMapper {

    // DTO request -> Hotel entity (for create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "numberOfRatings", ignore = true)
    public abstract Hotel toEntity(HotelRequestDto dto);

    // DTO request -> Hotel entity (for update - updates existing)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "numberOfRatings", ignore = true)
    public abstract void updateEntity(HotelRequestDto dto, @MappingTarget Hotel hotel);

    // Hotel entity -> response DTO (full info including rating)
    public abstract HotelResponseDto toResponseDto(Hotel hotel);
}
