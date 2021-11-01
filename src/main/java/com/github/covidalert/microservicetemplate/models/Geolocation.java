package com.github.covidalert.microservicetemplate.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Geolocation
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userId;

    private double latitude;

    private double longitude;

    private Long timestamp;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUserId()
    {
        return userId;
    }

    public Geolocation() {}

    public Geolocation(String userId, double latitude, double longitude, Long timestamp) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

}
