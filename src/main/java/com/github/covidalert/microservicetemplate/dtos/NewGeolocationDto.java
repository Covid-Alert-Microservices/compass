package com.github.covidalert.microservicetemplate.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NewGeolocationDto {

    @Max(90)
    @Min(-90)
    private double latitude;

    @Max(180)
    @Min(-180)
    private double longitude;

    @Min(0)
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
}
