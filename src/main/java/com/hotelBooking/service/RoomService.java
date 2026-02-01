package com.hotelBooking.service;

import com.hotelBooking.dto.RoomRequestDto;
import com.hotelBooking.dto.RoomResponseDto;
import com.hotelBooking.model.Hotel;
import com.hotelBooking.model.Room;
import com.hotelBooking.repository.HotelRepository;
import com.hotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public RoomResponseDto findById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return roomMapper.toResponseDto(room);
    }

    @Transactional
    public RoomResponseDto create(RoomRequestDto dto) {
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + dto.getHotelId()));

        Room room = roomMapper.toEntity(dto);
        room.setHotel(hotel);
        Room saved = roomRepository.save(room);
        return roomMapper.toResponseDto(saved);
    }

    @Transactional
    public RoomResponseDto update(Long id, RoomRequestDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + dto.getHotelId()));

        roomMapper.updateEntity(dto, room);
        room.setHotel(hotel);
        Room saved = roomRepository.save(room);
        return roomMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        roomRepository.deleteById(id);
    }

    // ============= Paginated search with filters (Task 10) =============
    @Transactional(readOnly = true)
    public Page<Room> findAllFiltered(Specification<Room> spec, Pageable pageable) {
        return roomRepository.findAll(spec, pageable);
    }
}
