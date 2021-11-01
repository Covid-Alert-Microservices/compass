package com.github.covidalert.microservicetemplate.repositories;

import com.github.covidalert.microservicetemplate.models.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeolocationRepository extends JpaRepository<Geolocation, Long>
{
    /**
     *  /!\ Degree threshold = 0.1 /!\
     *  Latitude  + threshold = deviation > 100m
     *  Longitude + threshold = deviation > 100m
     *
     *  Approx covid spreading distance = 8m
     */
    @Query(value = "" +
            "SELECT * " +
            "FROM Geolocation " +
            "WHERE userId NOT EQUALS :userId " +
            "AND timestamp >= :time" +
            "AND latitude - :latitude < 0.1 " +
            "AND longitude - :longitude < 0.1 OR ABSOLUTE(longitude - :longitude) > 359.9", nativeQuery = true)
    List<Geolocation> potentialInfectedGeolocations(@Param("userId") String userId, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("time") Long timestamp);
}
