package com.guppy.visualiserweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.visualiser.consumermq.KafkaMessageConsumer;
import com.guppy.visualiser.consumermq.data.MQRecord;
import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.GraphEvent;
import com.guppy.visualiserweb.data.model.HighlightEvent;
import com.guppy.visualiserweb.data.model.NetworkGraph;
import com.guppy.visualiserweb.data.model.Node;
import com.guppy.visualiserweb.data.model.NodeData;
import com.guppy.visualiserweb.data.model.SimulationOptions;
import com.guppy.visualiserweb.data.model.TimelineMessage;

@Controller
public class VisualiserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VisualiserController.class);

	NetworkGraph graph;

	boolean inSimualtion = false;

	ISimulator simulator = NetworkSimulator.getInstance();

	private List<String> faultyNodes = new ArrayList<>();

	@Autowired
	private SimpMessagingTemplate template;

	//private int faults = 2; // TODO

	@MessageMapping("/simulate")
	public void simulate(SimulationOptions options) throws Exception {

		// TODO
		// NetworkSimulator.getInstance().introduceLatencyInNetwork(20);
		// Handle nodes and create Id's for node for NetworkGraph
		Integer nodes;
		if (options.getNodes() != null) {
			nodes = options.getNodes(); // Accessing the nodes
		} else {
			// Alert the user
			template.convertAndSend("/topic/alert_updates", "Please Enter Number of nodes");
			return;
		}
		String implementation = options.getImplementation(); // Accessing the implementation
		int timeline = options.getTimeline(); // Accessing the time-line
		int acceptableFaults = options.getFaults(); // Accessing the faults

		LOGGER.info("Nodes :{} , Implementation selected {} , Latency {} and faults {}", 
				nodes, implementation, timeline,acceptableFaults);

		// Create Network Graph and publish it to front-end
		creatNetworkGraph(nodes);
		template.convertAndSend("/topic/simulate_data", graph);


		// Further processing based on nodes, implementation, and timeline
		// Start Simulation in a new thread
		new Thread(() -> {
			 startSimulation(nodes, implementation, acceptableFaults); // TODO latency is hard coded
		}).start();

		// start polling for messages from kafka
		KafkaMessageConsumer consumer = new KafkaMessageConsumer();
		try {
		    while (true) {
		        Iterable<ConsumerRecord<String, String>> records = consumer.consume();
		        for (ConsumerRecord<String, String> record : records) {
		            
		            try {
		                ObjectMapper objectMapper = new ObjectMapper();
		                // Deserialize the single MQRecord directly
		                MQRecord mqrecord = objectMapper.readValue(record.value(), MQRecord.class);
		                
		                if (mqrecord.getSenderId().getId().equals(mqrecord.getReceiverId().getId()) 
		                		&& !mqrecord.getEventType().equals("DELIVERED") 
		                		&& !mqrecord.getEventType().equals("NOTDELIVERED")) {
		                    continue; // Skip processing
		                }

		                mqrecord.setLeaderNode("node-0");
		                String constructedEdge = Strings.concat(mqrecord.getSenderId().getId(), mqrecord.getReceiverId().getId());
		                mqrecord.setEdgeHighlight(constructedEdge);

		                // Publish the single processed record to the front-end
		                template.convertAndSend("/topic/highlight_nodes", mqrecord);
		                
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		    }
		} finally {
		    consumer.close();
		    System.out.println("Closing");
		}


	}

	private void creatNetworkGraph(Integer nodes) {
		graph = new NetworkGraph();
		List<Node> nodeList = generateNodeNames(nodes);
		List<Edge> edges = generateNodeEdges(nodeList);
		Elements element = new Elements(nodeList, edges);
		graph.setElements(element);
	}

	// Create Edges : Construct the node names
	private static List<Node> generateNodeNames(int size) {
		List<Node> nodeNames = new ArrayList<Node>();
		NodeData data = new NodeData("node-0","leader");
		nodeNames.add(new Node(data));
		for (int i = 1; i < size; i++) {
			data = new NodeData("node-" + i,"");
			nodeNames.add(new Node(data));
		}
		return nodeNames;
	}

	// Create Edges : Construct the edge id's
	private List<Edge> generateNodeEdges(List<Node> nodeList) {
		List<Edge> edges = new ArrayList<Edge>();
		for (Node node1 : nodeList) {
			NodeData firstChar = node1.getNodeData();
			for (Node node2 : nodeList) {
				NodeData secondChar = node2.getNodeData();
				if (firstChar != secondChar) { // Exclude combinations with the same characters
					// System.out.println("" + firstChar + secondChar);
					EdgeData e = new EdgeData(Strings.concat(firstChar.getId(), secondChar.getId()), 1,
							firstChar.getId(), secondChar.getId());
					Edge edg = new Edge(e);
					edges.add(edg);
				}
			}
		}
		return edges;
	}


	public GraphEvent convertJsonToBroadcastEvent(String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("String Json :" + json);
		// json =
		// "{\"nodeNames\":[{\"id\":\"node-0\"},{\"id\":\"node-1\"},{\"id\":\"node-2\"},{\"id\":\"node-3\"},{\"id\":\"node-4\"}],\"senderId\":{\"id\":\"node-3\"},\"receiverId\":{\"id\":\"node-2\"},\"eventType\":\"ECHO\",\"timeStamp\":1691647089645}";
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
		simulator.introduceLatencyInNetwork(timelineMessage.getTimeline());
		// Add logic here to handle the timeline update as needed
	}

	private void startSimulation(int nodes, String implementation, int faults) {

		simulator.startSimulation(nodes, implementation, faults);
	}
	
	@MessageMapping("/stop_simulation")
	private void stopSimulation(){
		System.out.println("Stop Simulation");
		simulator.stopSimulation();
		
	}
	
	@MessageMapping("/inject_fault")
	private void injectFault(String nodeId) throws Exception{
		System.out.println("injecting fault to node : "+nodeId);
		if(! simulator.injectFault(nodeId)) {
			throw new Exception("Injecting Fault didnt work");
		}
		
	}
	
	@MessageMapping("/flood")
	private void flood(String nodeId) throws Exception{
		System.out.println("Flooding : "+nodeId);
		if(! simulator.flood(nodeId)) {
			throw new Exception("flooding didnt work");
		}
		
	}
	
	@MessageMapping("/drop_message")
	private void dropMessage(String nodeId) throws Exception{
		System.out.println("Drop Message at node : "+nodeId);
		if(! simulator.dropMessage(nodeId)) {
			throw new Exception("Dropping message didnt work");
		}
		
	}
	
	@MessageMapping("/alter_message")
	private void alterMessage(String nodeId) throws Exception{
		System.out.println("Alter Message content at  : "+nodeId);
		if(! simulator.alterMessage(nodeId)) {
			throw new Exception("Altering message didnt work");
		}
		
	}
	
	
	
}
