package com.example.datapulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// TODO : Remove the exclude when Postgres will be configured
@SpringBootApplication(scanBasePackages = {
		"com.example.datapulse.weather.*",
		"com.example.datapulse.common.exception"
})
@EnableJpaRepositories(basePackages = {"com.example.datapulse.weather.repository"})
@EntityScan(basePackages = {"com.example.datapulse.weather.models"})
public class DatapulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatapulseApplication.class, args);
	}

}
