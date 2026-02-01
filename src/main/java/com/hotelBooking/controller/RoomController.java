package com.hotelBooking.controller;

import com.hotelBooking.dto.*;
import com.hotelBooking.model.Room;
import com.hotelBooking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    // GET /api/rooms/{id}
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    // POST /api/rooms
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponseDto> create(@RequestBody RoomRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.create(dto));
    }

    // PUT /api/rooms/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponseDto> update(@PathVariable Long id, @RequestBody RoomRequestDto dto) {
        return ResponseEntity.ok(roomService.update(id, dto));
    }

    // DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/rooms/search — paginated + filtered search (Task 10)
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedResponseDto<RoomResponseDto>> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minGuests,
            @RequestParam(required = false) Integer maxGuests,
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var spec = RoomSpecification.filter(
                id, name, minPrice, maxPrice, minGuests, maxGuests, checkIn, checkOut, hotelId
        );

        Page<Room> roomPage = roomService.findAllFiltered(spec, pageable);
        List<RoomResponseDto> roomDtos = roomPage.getContent().stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());

        PaginatedResponseDto<RoomResponseDto> response = PaginatedResponseDto.<RoomResponseDto>builder()
                .data(roomDtos)
                .totalCount(roomPage.getTotalElements())
                .page(roomPage.getPageable().getPageNumber())
                .pageSize(roomPage.getPageable().getPageSize())
                .build();

        return ResponseEntity.ok(response);
    }
}
