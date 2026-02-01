package com.hotelBooking.service;

import com.hotelBooking.dto.HotelRequestDto;
import com.hotelBooking.dto.HotelResponseDto;
import com.hotelBooking.dto.RatingRequestDto;
import com.hotelBooking.model.Hotel;
import com.hotelBooking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public HotelResponseDto findById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return hotelMapper.toResponseDto(hotel);
    }

    @Transactional
    public HotelResponseDto create(HotelRequestDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        hotel.setRating(0.0);
        hotel.setNumberOfRatings(0);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toResponseDto(saved);
    }

    @Transactional
    public HotelResponseDto update(Long id, HotelRequestDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        hotelMapper.updateEntity(dto, hotel);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        hotelRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<HotelResponseDto> findAll() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ============= Rating update (Task 8) =============
    @Transactional
    public HotelResponseDto updateRating(Long id, RatingRequestDto ratingDto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));

        Integer newMark = ratingDto.getNewMark();
        if (newMark < 1 || newMark > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        double rating = hotel.getRating();
        int numberOfRatings = hotel.getNumberOfRatings();

        // totalRating = rating * numberOfRatings
        double totalRating = rating * numberOfRatings;
        // totalRating = totalRating - rating + newMark
        totalRating = totalRating - rating + newMark;
        // numberOfRatings stays same for the division
        // new rating = totalRating / numberOfRatings (but we need numberOfRatings+1 logic)
        // Actually per spec: rating = totalRating / numberOfRatings, then numberOfRatings++
        // But if numberOfRatings == 0 on first rating, we handle edge case
        int divisor = Math.max(numberOfRatings, 1);
        double newRating = Math.round((totalRating / divisor) * 10.0) / 10.0;

        hotel.setRating(newRating);
        hotel.setNumberOfRatings(numberOfRatings + 1);

        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toResponseDto(saved);
    }

    // ============= Paginated search with filters (Task 9) =============
    @Transactional(readOnly = true)
    public Page<Hotel> findAllFiltered(Specification<Hotel> spec, Pageable pageable) {
        return hotelRepository.findAll(spec, pageable);
    }
}
