package com.hotelBooking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRequestDto {
    private Integer newMark; // new rating from 1 to 5
}
