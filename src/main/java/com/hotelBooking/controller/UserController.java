package com.hotelBooking.controller;

import com.hotelBooking.dto.UserRequestDto;
import com.hotelBooking.dto.UserResponseDto;
import com.hotelBooking.service.UserService;
import com.hotelBooking.statistics.model.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;

    @Value("${app.kafka.topic.user-registration}")
    private String userRegistrationTopic;

    // POST /api/users/register — open endpoint (no auth)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto dto) {
        UserResponseDto created = userService.create(dto);

        // Send Kafka event for statistics
        UserRegistrationEvent event = UserRegistrationEvent.builder()
                .userId(created.getId())
                .build();
        kafkaTemplate.send(userRegistrationTopic, String.valueOf(created.getId()), event);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
