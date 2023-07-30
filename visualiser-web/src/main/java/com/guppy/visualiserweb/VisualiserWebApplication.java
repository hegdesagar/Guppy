package com.guppy.visualiserweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example","com.guppy"})
public class VisualiserWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisualiserWebApplication.class, args);
	}

}
