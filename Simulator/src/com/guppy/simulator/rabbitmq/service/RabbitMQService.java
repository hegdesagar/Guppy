package com.guppy.simulator.rabbitmq.service;

<<<<<<< HEAD
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

	public void publishMessage(BroadcastEvent event) throws Exception {
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonEvent = mapper.writeValueAsString(event);
	    channel.basicPublish(exchangeName, "", null, jsonEvent.getBytes("UTF-8"));
	    System.out.println(" [x] Sent '" + jsonEvent + "'");
=======
import com.guppy.simulator.broadcast.message.IMessage;
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

	public void publishMessage(IMessage message) throws Exception {
		channel.basicPublish(exchangeName, "", null, message.getContent().toString().getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");
>>>>>>> branch 'master' of https://github.com/hegdesagar/Guppy.git
	}
}
