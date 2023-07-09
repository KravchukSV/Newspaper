package com.example.newspaper;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Newspaper",
				description = "A RESTful API application for publishing articles",
				version = "v.1.0",
				contact = @Contact(
						name = "Serhii",
						email = "kravchuk.s.v.1@gmail.com"
				)
		)
)
@SpringBootApplication
public class NewspaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewspaperApplication.class, args);
	}

}
