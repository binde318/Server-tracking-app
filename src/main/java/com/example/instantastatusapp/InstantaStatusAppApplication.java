package com.example.instantastatusapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InstantaStatusAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstantaStatusAppApplication.class, args);
	}


}
