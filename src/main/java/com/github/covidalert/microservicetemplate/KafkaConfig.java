package com.github.covidalert.microservicetemplate;

import com.github.covidalert.microservicetemplate.dtos.UserPositiveDto;
import com.github.covidalert.microservicetemplate.models.Geolocation;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public NewTopic geolocationCreated() {
        return TopicBuilder.name("geolocation_created").config(TopicConfig.RETENTION_MS_CONFIG, "1").build();
    }

    @Bean
    public NewTopic userPositive() {
        return TopicBuilder.name("user_positive").config(TopicConfig.RETENTION_MS_CONFIG, "1").build();
    }

    /* Producers */

    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Geolocation> jsonProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Geolocation> jsonKafkaTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    /* Consumers */

    @Bean
    public ConsumerFactory<String, Geolocation> geolocationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"covid-alert");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Geolocation.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Geolocation> geolocationKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Geolocation>();
        factory.setConsumerFactory(geolocationConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserPositiveDto> userConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"covid-alert");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(UserPositiveDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserPositiveDto> userKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserPositiveDto>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }


}
