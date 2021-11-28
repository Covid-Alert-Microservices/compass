package com.github.covidalert.compass.listeners;

import com.github.covidalert.compass.Helper;
import com.github.covidalert.compass.models.Geolocation;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import com.github.covidalert.compass.repositories.GraphRepository;
import com.github.covidalert.covidtests.dtos.UserPositiveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeoListener {
    public static final int VIRUS_TIME_MILLIS = 5 * 60 * 1000; // 5 minutes
    public static final int VIRUS_DISTANCE_METERS = 8;
    public static final int INCUBATION_TIME_MILLIS = 9 * 24 * 60 * 60 * 1000; // 9 days

    private final Logger logger = LoggerFactory.getLogger(GeoListener.class);

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @KafkaListener(topics = "user_positive", containerFactory = "userKafkaListener")
    public void userPositive(UserPositiveDTO userPositiveDto) {
        var potentialIds = graphRepository.findPotentials(userPositiveDto.getUserId(), userPositiveDto.getTimestamp() - INCUBATION_TIME_MILLIS);
        this.logger.debug(String.format(
                "Received new positive user for %s from Kafka topic `user_positive`. %d users potentially infected.",
                userPositiveDto.getUserId(),
                potentialIds.size()
        ));
        for (var id : potentialIds) {
            this.stringKafkaTemplate.send("send_alert", id);
            this.logger.trace(String.format("Sent message on `send_alert` kafka topic for user %s.", id));
        }
    }

    @KafkaListener(topics = "geolocation_created", containerFactory = "geolocationKafkaListener")
    public void populateGraph(Geolocation geolocation) {
        List<Geolocation> potentialInfectedGeolocations = geolocationRepository.potentialInfectedGeolocations(geolocation.getUserId(), geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getTimestamp() - VIRUS_TIME_MILLIS);
        int insertedCount = 0;
        for (Geolocation geo : potentialInfectedGeolocations) {
            double distance = Helper.distance(geolocation.getLatitude(), geo.getLatitude(), geolocation.getLongitude(), geo.getLongitude());
            this.logger.trace(String.format(
                    "Geolocation %f/%f (user %s) is %f meters away from %f/%f (user %s). Will insert if %f <= %d",
                    geo.getLatitude(),
                    geo.getLongitude(),
                    geo.getUserId(),
                    distance,
                    geolocation.getLatitude(),
                    geolocation.getLongitude(),
                    geolocation.getUserId(),
                    distance,
                    VIRUS_DISTANCE_METERS
            ));
            if (distance <= VIRUS_DISTANCE_METERS) {
                graphRepository.insert(geolocation.getUserId(), geo.getUserId(), geolocation.getTimestamp());
                insertedCount += 1;
            }
        }
        this.logger.debug(String.format(
                "Found %d potential infected geolocations around user %s. Inserted %d/%d in graph.",
                potentialInfectedGeolocations.size(),
                geolocation.getUserId(),
                insertedCount,
                potentialInfectedGeolocations.size()
        ));
    }

}


