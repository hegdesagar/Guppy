package com.guppy.visualiserweb;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Initializes the Servlet for the Spring Boot application.
 * This class allows the Spring Boot application to be deployed as a WAR file in an external servlet container.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
     * Configures the application. Called by Spring's Servlet infrastructure.
     * 
     * @param application the builder for the application context
     * @return the application builder with sources
     */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VisualiserWebApplication.class);
	}

}
