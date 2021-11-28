package com.github.covidalert.compass.listeners;

import com.github.covidalert.compass.Helper;
import com.github.covidalert.compass.models.Geolocation;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import com.github.covidalert.compass.repositories.GraphRepository;
import com.github.covidalert.covidtests.dtos.UserPositiveDTO;
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

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @KafkaListener(topics = "user_positive", containerFactory = "userKafkaListener")
    public void userPositive(UserPositiveDTO userPositiveDto) {
        List<String> potentialIds = graphRepository.findPotentials(userPositiveDto.getUserId(), userPositiveDto.getTimestamp() - INCUBATION_TIME_MILLIS);
        for (String id : potentialIds) {
            this.stringKafkaTemplate.send("send_alert", id);
        }
    }

    @KafkaListener(topics = "geolocation_created", containerFactory = "geolocationKafkaListener")
    public void populateGraph(Geolocation geolocation) {
        List<Geolocation> potentialInfectedGeolocations = geolocationRepository.potentialInfectedGeolocations(geolocation.getUserId(), geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getTimestamp() - VIRUS_TIME_MILLIS);
        for (Geolocation geo : potentialInfectedGeolocations) {
            double distance = Helper.distance(geolocation.getLatitude(), geolocation.getLongitude(), geo.getLatitude(), geo.getLongitude());
            if (distance <= VIRUS_DISTANCE_METERS) {
                graphRepository.insert(geolocation.getUserId(), geo.getUserId(), geolocation.getTimestamp());
            }
        }
    }

}


