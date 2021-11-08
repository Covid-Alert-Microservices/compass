package com.github.covidalert.microservicetemplate;

import com.github.covidalert.microservicetemplate.repositories.GeolocationRepository;
import com.github.covidalert.microservicetemplate.repositories.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableScheduling
public class WebConfig implements WebMvcConfigurer
{
    public static final int CLEANUP_TIME_MILLIS = 30 * 24 * 60 * 60 * 1000; // 30 days

    @Autowired
    private GeolocationRepository geolocationRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**");
    }

    @Scheduled(cron = "0 0 0 * * *")
    void dailyCleanup(){
        Long timestamp = System.currentTimeMillis() - CLEANUP_TIME_MILLIS;
        try {
            geolocationRepository.cleanFrom(timestamp);
        } catch (Exception e){
            System.out.println(e);
        }
        try {
            graphRepository.cleanRelationshipsFrom(timestamp);
            graphRepository.cleanAloneNodes();
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
