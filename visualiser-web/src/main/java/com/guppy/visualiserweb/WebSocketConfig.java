package com.guppy.visualiserweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for the WebSocket and STOMP based messaging.
 * This class sets up message broker topics and application prefixes 
 * as well as defines the STOMP endpoint for WebSocket communication.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
     * Configures the message broker for WebSocket communication.
     * Enables a simple in-memory broker based on the "/topic" prefix and 
     * sets application specific destination prefixes.
     * 
     * @param config the message broker registry.
     */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	 /**
     * Registers STOMP protocol endpoints.
     * Specifies the WebSocket endpoint that the client uses to connect.
     * 
     * @param registry the STOMP endpoint registry.
     */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/gs-guide-websocket");
	}

}