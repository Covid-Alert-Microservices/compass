package com.github.covidalert.microservicetemplate.controllers;

import com.github.covidalert.microservicetemplate.dtos.NewGeolocationDto;
import com.github.covidalert.microservicetemplate.models.Geolocation;
import com.github.covidalert.microservicetemplate.repositories.GeolocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/geo")
public class GeoController
{

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Autowired
    private KafkaTemplate<String, Geolocation> geolocationKafkaTemplate;

    @PostMapping
    public Geolocation addUserGeolocation(Principal principal, @Valid @RequestBody NewGeolocationDto geolocation )
    {
        String userId = "randomId1";
        if(principal != null && principal.getName().length() > 0) userId = principal.getName();
        Geolocation document = new Geolocation(userId, geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getTimestamp());
        Geolocation saved =  geolocationRepository.save(document);
        geolocationKafkaTemplate.send("geolocation_created", document);
        return saved;
    }

}
