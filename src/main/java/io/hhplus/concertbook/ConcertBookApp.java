package io.hhplus.concertbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConcertBookApp {

	public static void main(String[] args) {
		SpringApplication.run(ConcertBookApp.class, args);
	}

}
