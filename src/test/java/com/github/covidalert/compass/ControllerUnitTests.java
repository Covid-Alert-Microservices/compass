package com.github.covidalert.compass;

import com.github.covidalert.compass.controllers.GeoController;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = GeoController.class)
public class ControllerUnitTests {
    private final Principal principal = () -> "user-id";

    @Autowired
    private GeoController geoController;

    @MockBean
    private GeolocationRepository geolocationRepository;

    @Test
    public void testControllerIsNotNull() {
        assertThat(geoController).isNotNull();
    }

}
