package com.leviberga.fluxpay.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.errorHandler(new DefaultResponseErrorHandler(){
            @Override
            public boolean hasError (HttpStatusCode statusCode){
                return false;
            }
        }).build();
    }
}
