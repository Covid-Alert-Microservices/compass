package com.github.covidalert.microservicetemplate;

import com.github.covidalert.microservicetemplate.models.Geolocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class GeolocationApplicationTests
{

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Geolocation> geolocationKafkaTemplate;

    @Test
    public void kafkaTest() throws Exception {
        geolocationKafkaTemplate.send("geolocation_created", new Geolocation("12345",10.000,10.000,1L));
        stringKafkaTemplate.send("user_positive", "12345");
    }

}
