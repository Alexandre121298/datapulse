package com.example.datapulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// TODO : Remove the exclude when Postgres will be configured
@SpringBootApplication(scanBasePackages = {
		"com.example.datapulse.weather.*",
		"com.example.datapulse.common.exception",
		"com.example.datapulse.config.*"
})
public class DatapulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatapulseApplication.class, args);
	}

}
