package com.hotelBooking.statistics.service;

import com.hotelBooking.statistics.model.StatisticsEvent;
import com.hotelBooking.statistics.repository.StatisticsRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public ByteArrayInputStream exportToCsv() {
        List<StatisticsEvent> events = statisticsRepository.findAll();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            // Write header
            String[] header = {"ID", "Event Type", "User ID", "Check In", "Check Out", "Created At"};
            writer.writeNext(header);

            // Write data
            for (StatisticsEvent event : events) {
                String[] record = new String[]{
                        event.getId() != null ? event.getId().toString() : "",
                        event.getEventType() != null ? event.getEventType() : "",
                        event.getUserId() != null ? event.getUserId().toString() : "",
                        event.getCheckIn() != null ? event.getCheckIn() : "",
                        event.getCheckOut() != null ? event.getCheckOut() : "",
                        event.getCreatedAt() != null ? event.getCreatedAt().toString() : ""
                };
                writer.writeNext(record);
            }

            writer.flush();
            writer.close();

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (Exception e) {
            log.error("Error exporting statistics to CSV", e);
            throw new RuntimeException("Failed to export statistics to CSV", e);
        }
    }
}
