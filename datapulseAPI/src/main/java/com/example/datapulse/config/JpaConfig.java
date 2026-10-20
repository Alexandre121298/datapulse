package com.example.datapulse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
@Configuration
@EnableJpaRepositories(basePackages = {
        "com.example.datapulse.weather.repository",
        "com.example.datapulse.user.repository"
})
@EntityScan(basePackages = {
        "com.example.datapulse.weather.models",
        "com.example.datapulse.user.models"
})
public class JpaConfig {
}