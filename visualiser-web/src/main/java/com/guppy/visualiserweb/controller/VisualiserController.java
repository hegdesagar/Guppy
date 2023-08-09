package com.guppy.visualiserweb.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.NetworkGraph;
import com.guppy.visualiserweb.data.model.Node;
import com.guppy.visualiserweb.data.model.NodeData;
import com.guppy.visualiserweb.data.model.SimulationOptions;
import com.guppy.visualiserweb.data.model.TimelineMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@Controller
public class VisualiserController {

	NetworkGraph graph = new NetworkGraph();

	boolean inSimualtion = false;

	ISimulator simulator = NetworkSimulator.getInstance();

	private final static String QUEUE_NAME = "SIMULATION-QUEUE";

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/simulate")
	public void simulate(SimulationOptions options) throws Exception {
		System.out.println("In simulation:");
		Integer nodes;
		if (options.getNodes() != null) {
			nodes = options.getNodes(); // Accessing the nodes
		} else {
			nodes = 2;
		}
		String implementation = options.getImplementation(); // Accessing the implementation
		int timeline = options.getTimeline(); // Accessing the timeline

		// TODO remove these after testing
		System.out.println("Nodes: " + nodes);
		System.out.println("Implementation: " + implementation);
		System.out.println("Timeline: " + timeline);

		// Create RabbitMQ connection

		// Initialize the queue
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// Declare the exchange
		channel.exchangeDeclare(QUEUE_NAME, "fanout");

		// Declare a queue (can be a named queue or an anonymous one)
		String queueName = channel.queueDeclare().getQueue();

		// Bind the queue to the fanout exchange
		channel.queueBind(queueName, QUEUE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
		    System.out.println(" [x] Received '" + message + "'");
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
		// Further processing based on nodes, implementation, and timeline
		// Start Simulation
		// Start Simulation in a new thread
		new Thread(() -> {
			startSimulation(nodes, implementation, timeline);
		}).start();

		// Start a new thread to listen to the queue

		// startSimulation(nodes, implementation, timeline);

		System.out.println("Returning from simulation:");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		/*
		 * while (true) {
		 * 
		 * channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		 * // Thread.sleep(1000); // simulated delay of 1s // Sending the data whenever
		 * the NetworkGraph object changes
		 * 
		 * // template.convertAndSend("/topic/simulate_data", graph); }
		 */

	}

	@MessageMapping("/highlight")
	public void highlightNodes() throws Exception {
		// replace with your logic for determining which nodes to highlight
		List<String> nodesToHighlight = Arrays.asList("a");
		template.convertAndSend("/topic/highlight_nodes", nodesToHighlight);
	}

	@MessageMapping("/update_timeline")
	public void updateTimeline(TimelineMessage timelineMessage) throws Exception {
		System.out.println("Updated timeline: " + timelineMessage.getTimeline());

		// Add logic here to handle the timeline update as needed
	}

	private void startSimulation(int nodes, String implementation, int faults) {

		simulator.startSimulation(nodes, implementation, faults);
	}
}
