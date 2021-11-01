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
    private KafkaTemplate<String, Geolocation> template;

    @PostMapping
    public Geolocation addUserGeolocation(Principal principal, @Valid @RequestBody NewGeolocationDto geolocation )
    {
        Geolocation document = new Geolocation("ifemogieogi", geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getTimestamp());
        Geolocation saved =  geolocationRepository.save(document);
        System.out.println(saved);
        System.out.println(saved.getClass());
        template.send("geolocation_created", saved);
        return saved;
    }

}
