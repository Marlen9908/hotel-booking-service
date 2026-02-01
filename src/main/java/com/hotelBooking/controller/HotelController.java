package com.hotelBooking.controller;

import com.hotelBooking.dto.*;
import com.hotelBooking.model.Hotel;
import com.hotelBooking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    // GET /api/hotels/{id}
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HotelResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.findById(id));
    }

    // POST /api/hotels
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponseDto> create(@RequestBody HotelRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(dto));
    }

    // PUT /api/hotels/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponseDto> update(@PathVariable Long id, @RequestBody HotelRequestDto dto) {
        return ResponseEntity.ok(hotelService.update(id, dto));
    }

    // DELETE /api/hotels/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/hotels — list of all hotels
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HotelResponseDto>> findAll() {
        return ResponseEntity.ok(hotelService.findAll());
    }

    // PUT /api/hotels/{id}/rating — update rating (Task 8)
    @PutMapping("/{id}/rating")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HotelResponseDto> updateRating(@PathVariable Long id, @RequestBody RatingRequestDto dto) {
        return ResponseEntity.ok(hotelService.updateRating(id, dto));
    }

    // GET /api/hotels/search — paginated + filtered search (Task 9)
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedResponseDto<HotelResponseDto>> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String headline,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minDistance,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) Integer minNumberOfRatings,
            @RequestParam(required = false) Integer maxNumberOfRatings,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var spec = HotelSpecification.filter(
                id, name, headline, city, address,
                minDistance, maxDistance, minRating, maxRating,
                minNumberOfRatings, maxNumberOfRatings
        );

        Page<Hotel> hotelPage = hotelService.findAllFiltered(spec, pageable);
        List<HotelResponseDto> hotelDtos = hotelPage.getContent().stream()
                .map(hotelMapper::toResponseDto)
                .collect(Collectors.toList());

        PaginatedResponseDto<HotelResponseDto> response = PaginatedResponseDto.<HotelResponseDto>builder()
                .data(hotelDtos)
                .totalCount(hotelPage.getTotalElements())
                .page(hotelPage.getPageable().getPageNumber())
                .pageSize(hotelPage.getPageable().getPageSize())
                .build();

        return ResponseEntity.ok(response);
    }
}
