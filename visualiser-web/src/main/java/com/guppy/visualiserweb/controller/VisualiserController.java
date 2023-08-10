package com.guppy.visualiserweb.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.GraphEvent;
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

	NetworkGraph graph;

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
			//Alert the user
			template.convertAndSend("/topic/alert_updates", "Please Enter Number of nodes");
			return;
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
		//TODO- -------------------------------------------------------
		//processGraph("");
		//template.convertAndSend("/topic/simulate_data", graph);
		//TODO END- -------------------------------------------------------
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			if (delivery != null) {
		        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
		        // System.out.println(" [x] Received '" + message + "'");
		        if(message !=null) {
		        graph = processGraph(message);
		        //ObjectMapper objectMapper = new ObjectMapper();
		        //String jsonString = objectMapper.writeValueAsString(graph);
		        template.convertAndSend("/topic/simulate_data", graph);
		        }
		    } else {
		        // Handle the case when delivery is null
		        System.err.println("Delivery is null!");
		    }
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


	}

	private NetworkGraph processGraph(String message) {
		try {
			GraphEvent eventMessage = convertJsonToBroadcastEvent(message);
			//System.out.println("NodeNames : "+eventMessage.getEventType());
			//Check if the graph is created
			if(graph == null) {
				graph = new NetworkGraph();
				//The line of code is executed only once
				List<Node> nodeList = new ArrayList<Node>();
				List<Edge> edges = new ArrayList<Edge>();
				//Create nodes
				for(NodeData n : eventMessage.getNodeNames()) {
					NodeData data = new NodeData(n.getId());
					nodeList.add(new Node(data));
				}
				//Create Edges : Construct the edge id's
				 for (NodeData firstChar : eventMessage.getNodeNames()) {
			            for (NodeData secondChar : eventMessage.getNodeNames()) {
			                if (firstChar != secondChar) { // Exclude combinations with the same characters
			                  //  System.out.println("" + firstChar + secondChar);
			                	EdgeData e = new EdgeData(Strings.concat(firstChar.getId(),secondChar.getId()),1,firstChar.getId(),secondChar.getId());
			                	Edge edg = new Edge(e);
			                	edges.add(edg);
			                }
			            }
				 }
				 //Construct the graph Element
				 Elements element = new Elements(nodeList,edges);
				 graph.setElements(element);
			}
			//Hightlight the sender node
			graph.setHighlightedNode(eventMessage.getSenderId().getId());
			//construct edge
			String construtedEdge = Strings.concat(eventMessage.getSenderId().getId(), eventMessage.getReceiverId().getId());
			if(eventMessage.getEventType().equals("ECHO")) {
				graph.setHighlightEchoEdge(construtedEdge);
			}else if(eventMessage.getEventType().equals("SEND")){
				graph.setHighlightSendEdge(construtedEdge);
			}else {
				System.out.println("Some error in ECHO or SEND");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return graph;
	}
	
	public GraphEvent convertJsonToBroadcastEvent(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("String Json :"+json);
        //json =  "{\"nodeNames\":[{\"id\":\"node-0\"},{\"id\":\"node-1\"},{\"id\":\"node-2\"},{\"id\":\"node-3\"},{\"id\":\"node-4\"}],\"senderId\":{\"id\":\"node-3\"},\"receiverId\":{\"id\":\"node-2\"},\"eventType\":\"ECHO\",\"timeStamp\":1691647089645}";
        GraphEvent event = objectMapper.readValue(json, GraphEvent.class);
        return event;
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
