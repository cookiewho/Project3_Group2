package com.example.rocketcorner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RocketcornerApplication {

	public static void main(String[] args) {

		SpringApplication.run(RocketcornerApplication.class, args);

		System.out.println("\n\n\nRob look here\n\n\n");
		System.out.println(System.getenv("TEST"));
	}

}
