package com.github.covidalert.compass.controllers;

import com.github.covidalert.compass.dtos.NewGeolocationDto;
import com.github.covidalert.compass.models.Geolocation;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/api")
public class GeoController
{

    private final Logger logger = LoggerFactory.getLogger(GeoController.class);

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Autowired
    private KafkaTemplate<String, Geolocation> geolocationKafkaTemplate;

    @PostMapping
    public Geolocation addUserGeolocation(Principal principal, @Valid @RequestBody NewGeolocationDto geolocation)
    {
        String userId = "123-456";
        if (principal != null && principal.getName().length() > 0)
        {
            userId = principal.getName();
        }
        else
        {
            this.logger.warn(String.format("New geolocation from unknown user. Falling back to id %s.", userId));
        }
        Geolocation document = new Geolocation(userId, geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getTimestamp());
        Geolocation saved = geolocationRepository.save(document);
        geolocationKafkaTemplate.send("geolocation_created", document);
        return saved;
    }

}
