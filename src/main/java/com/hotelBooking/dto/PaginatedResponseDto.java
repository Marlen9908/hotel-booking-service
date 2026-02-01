package com.hotelBooking.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponseDto<T> {
    private List<T> data;
    private Long totalCount;
    private Integer page;
    private Integer pageSize;
}
