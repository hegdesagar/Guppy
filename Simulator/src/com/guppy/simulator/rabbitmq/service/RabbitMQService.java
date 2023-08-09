package com.guppy.simulator.rabbitmq.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQService {

	private final Channel channel;
	private final String exchangeName;

	public RabbitMQService(String exchangeName) throws Exception {
		this.exchangeName = exchangeName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); // Change this as needed
		Connection connection = factory.newConnection();
		this.channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName, "fanout"); // Creating a fanout exchange
	}

	public void publishMessage(BroadcastEvent event)  {
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonEvent;
		try {
			jsonEvent = mapper.writeValueAsString(event);
			channel.basicPublish(exchangeName, "", null, jsonEvent.getBytes("UTF-8"));
			//System.out.println(" [x] Sent '" + jsonEvent + "'");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    

	}
}
