package com.guppy.visualiserweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application's main entry point for the Visualiser Web Application.
 * This class sets up the application to scan components within the specified packages.
 */
@SpringBootApplication
@ComponentScan({"com.example","com.guppy"})
public class VisualiserWebApplication {

	/**
     * Main method which serves as the entry point for the Spring Boot application.
     * @param args command-line arguments passed to the application.
     */
	public static void main(String[] args) {
		SpringApplication.run(VisualiserWebApplication.class, args);
	}

}
