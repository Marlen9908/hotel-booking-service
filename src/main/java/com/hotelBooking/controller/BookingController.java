package com.hotelBooking.controller;

import com.hotelBooking.dto.BookingRequestDto;
import com.hotelBooking.dto.BookingResponseDto;
import com.hotelBooking.dto.PaginatedResponseDto;
import com.hotelBooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // POST /api/bookings — create a booking
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(dto));
    }

    // GET /api/bookings — get all bookings (admin only), paginated (Task 10 update)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<BookingResponseDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BookingResponseDto> bookingPage = bookingService.getAllBookings(pageable);

        PaginatedResponseDto<BookingResponseDto> response = PaginatedResponseDto.<BookingResponseDto>builder()
                .data(bookingPage.getContent())
                .totalCount(bookingPage.getTotalElements())
                .page(bookingPage.getPageable().getPageNumber())
                .pageSize(bookingPage.getPageable().getPageSize())
                .build();

        return ResponseEntity.ok(response);
    }
}
