package io.hhplus.concertbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories
//@ComponentScan("com.helloworld.quickstart.repository")
public class ConcertBookApp {

	public static void main(String[] args) {
		SpringApplication.run(ConcertBookApp.class, args);
	}

}