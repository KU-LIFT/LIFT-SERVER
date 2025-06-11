package com.kulift.lift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiftApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiftApplication.class, args);
	}

}
