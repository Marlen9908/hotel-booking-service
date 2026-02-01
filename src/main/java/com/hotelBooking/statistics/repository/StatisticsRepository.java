package com.hotelBooking.statistics.repository;

import com.hotelBooking.statistics.model.StatisticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends MongoRepository<StatisticsEvent, String> {

    List<StatisticsEvent> findAll();
}
