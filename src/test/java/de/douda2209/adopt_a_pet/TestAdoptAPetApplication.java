package de.douda2209.adopt_a_pet;

import org.springframework.boot.SpringApplication;

public class TestAdoptAPetApplication {

	public static void main(String[] args) {
		SpringApplication.from(AdoptAPetApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
