package com.github.covidalert.compass;

import com.github.covidalert.compass.controllers.GeoController;
import com.github.covidalert.compass.dtos.NewGeolocationDto;
import com.github.covidalert.compass.models.Geolocation;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Keycloak off
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class ControllerUnitTests {
    class PrincipalImpl implements Principal {
        @Override
        public String getName() { return "123-456"; }
    }

    final Geolocation mockGeolocation = new Geolocation("123-456", 1L, 1L, 1L);
    final NewGeolocationDto mockGeolocationDto = new NewGeolocationDto(1L, 1L, 1L);
    final Principal principal = new PrincipalImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoController geoController;

    @MockBean
    private GeolocationRepository geolocationRepository;

    @MockBean
    private KafkaConfig kafkaConfig;

    @MockBean
    private KafkaTemplate<String, Geolocation> geolocationKafkaTemplate;

    @Test
    public void testPostPosition() throws Exception {
        // given
        when(geoController.addUserGeolocation(principal, mockGeolocationDto)).thenReturn(mockGeolocation);

        //when
        mockMvc.perform(post("/api/", mockGeolocationDto))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").value(mockGeolocation))
        .andExpect(jsonPath("$").isNotEmpty());
    }

}
