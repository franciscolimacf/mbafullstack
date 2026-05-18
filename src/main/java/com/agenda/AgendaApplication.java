package com.agenda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AgendaApplication {
	 static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
		log.info("Aplicação iniciada!!!");
	}

}
