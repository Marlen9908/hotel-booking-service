package com.hotelBooking.dto;

import com.hotelBooking.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String username;
    private String password;
    private String email;
    private Role role;
}
