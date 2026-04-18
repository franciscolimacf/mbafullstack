package com.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// classe principal - so starta a aplicacao
@SpringBootApplication
public class AgendaApplication {

	// metodo main que roda tudo
	public static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
		System.out.println("aplicacao iniciada!!!");
	}

}
