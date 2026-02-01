package com.hotelBooking.service;

import com.hotelBooking.dto.UserRequestDto;
import com.hotelBooking.dto.UserResponseDto;
import com.hotelBooking.model.User;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
public abstract class UserMapper {

    // DTO request -> User entity
    @Mapping(target = "id", ignore = true)
    public abstract User toEntity(UserRequestDto dto);

    // Update existing entity
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntity(UserRequestDto dto, @MappingTarget User user);

    // User entity -> response DTO (password excluded)
    public abstract UserResponseDto toResponseDto(User user);
}
