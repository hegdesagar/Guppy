/*
====================================================
Copyright (c) 2023 SagarH
All Rights Reserved.
Permission to use, copy, modify, and distribute this software and its
documentation for any purpose, without fee, and without a written agreement is hereby granted, 
provide that the above copyright notice and this paragraph and the following two paragraphs appear in all copies.

IN NO EVENT SHALL YOUR NAME BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF YOU HAVE BEEN
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

SagarH SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND YOUR NAME HAS NO
OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
====================================================
*/
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.visualiser.consumermq.KafkaMessageConsumer;
import com.guppy.visualiser.consumermq.data.MQRecord;
import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.GraphEvent;
import com.guppy.visualiserweb.data.model.NetworkGraph;
import com.guppy.visualiserweb.data.model.Node;
import com.guppy.visualiserweb.data.model.NodeData;
import com.guppy.visualiserweb.data.model.SimulationOptions;
import com.guppy.visualiserweb.data.model.TimelineMessage;


/**
 * The main controller for the visualization operations related to network simulations.
 * It handles various simulation options, message consumption from Kafka, and real-time updates 
 * to the front-end via web sockets.
 * 
 * @author HegdeSagar
 */
@Controller
public class VisualiserController {

	//Initialize Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(VisualiserController.class);

	//Network Graph
	NetworkGraph graph;

	//Flag to determine if the simulation is in progress
	boolean inSimualtion = false;

	//Network Simulator instance
	ISimulator simulator = NetworkSimulator.getInstance();

	@Autowired
	private SimpMessagingTemplate template;

	
	/**
     * Initiates the simulation based on the given options.
     * 
     * @param options The simulation options which include the number of nodes, 
     *                implementation type, time-line, and acceptable faults.
     * @throws Exception If any error occurs during the simulation or processing.
     */
	@MessageMapping("/simulate")
	public void simulate(SimulationOptions options) throws Exception {

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

		// Start Simulation in a new thread
		new Thread(() -> {
			 startSimulation(nodes, implementation, acceptableFaults);
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
		                String constructedEdge = Strings.concat(mqrecord.getSenderId().getId(),
		                		mqrecord.getReceiverId().getId());
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
		}
	}
	
	/**
     * Creates the network of nodes and edges for the visualiser.
     * 
     * @param nodes Number of nodes in the network
     */
	private void creatNetworkGraph(Integer nodes) {
		graph = new NetworkGraph();
		List<Node> nodeList = generateNodeNames(nodes);
		List<Edge> edges = generateNodeEdges(nodeList);
		Elements element = new Elements(nodeList, edges);
		graph.setElements(element);
	}

	/**
     * Generate node names. 
     * Nodes have prefix "node-" and suffix ranging from 0 to n (no of nodes -1) 
     * 
     * @param size Number of nodes in the network
     */
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

	/**
     * Create Edges : Construct the edge id's
     * 
     * @param nodeList list of nodes
     */
	private List<Edge> generateNodeEdges(List<Node> nodeList) {
		List<Edge> edges = new ArrayList<Edge>();
		for (Node node1 : nodeList) {
			NodeData firstChar = node1.getNodeData();
			for (Node node2 : nodeList) {
				NodeData secondChar = node2.getNodeData();
				if (firstChar != secondChar) { // Exclude combinations with the same characters
					EdgeData e = new EdgeData(Strings.concat(firstChar.getId(), secondChar.getId()), 1,
							firstChar.getId(), secondChar.getId());
					Edge edg = new Edge(e);
					edges.add(edg);
				}
			}
		}
		return edges;
	}


	/**
     * Object Map from json to {@link GraphEvent}
     * 
     * @param json JSON string
     * @return {@link GraphEvent} object.
     * @throws IOException if there is an error during the mapping process.
     */
	public GraphEvent convertJsonToBroadcastEvent(String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("String Json :" + json);
		GraphEvent event = objectMapper.readValue(json, GraphEvent.class);
		return event;
	}

	/**
     * Endpoint to highlight nodes. Triggers the logic to determine which nodes should be highlighted
     * and sends the result to the subscribed clients.
     * 
     * @throws Exception If any error occurs during the process.
     */
	@MessageMapping("/highlight")
	public void highlightNodes() throws Exception {
		List<String> nodesToHighlight = Arrays.asList("a");
		template.convertAndSend("/topic/highlight_nodes", nodesToHighlight);
	}

	/**
     * Endpoint to update the simulation timeline. Updates the network latency based on the provided timeline.
     * 
     * @param timelineMessage Object containing the new timeline details.
     * @throws Exception If any error occurs during the process.
     */
	@MessageMapping("/update_timeline")
	public void updateTimeline(TimelineMessage timelineMessage) throws Exception {
		System.out.println("Updated timeline: " + timelineMessage.getTimeline());
		simulator.introduceLatencyInNetwork(timelineMessage.getTimeline());
	}

	/**
     * Initiates a simulation process based on provided parameters.
     * 
     * @param nodes The number of nodes for the simulation.
     * @param implementation The broadcasting strategy to use.
     * @param faults The acceptable number of faults.
     */
	private void startSimulation(int nodes, String implementation, int faults) {

		simulator.startSimulation(nodes, implementation, faults);
	}
	
	/**
     * Endpoint to stop the ongoing simulation.
     */
	@MessageMapping("/stop_simulation")
	private void stopSimulation(){
		simulator.stopSimulation();
		
	}
	
	/**
     * Endpoint to inject faults into a specific node in the simulation.
     * 
     * @param nodeId The ID of the node where the fault needs to be injected.
     * @throws Exception If fault injection fails.
     */
	@MessageMapping("/inject_fault")
	private void injectFault(String nodeId) throws Exception{
		if(! simulator.injectFault(nodeId)) {
			throw new Exception("Injecting Fault didnt work");
		}
		
	}
	
	/**
     * Endpoint to flood a specific node in the simulation.
     * 
     * @param nodeId The ID of the node to flood.
     * @throws Exception If flooding operation fails.
     */
	@MessageMapping("/flood")
	private void flood(String nodeId) throws Exception{
		if(! simulator.flood(nodeId)) {
			throw new Exception("flooding didnt work");
		}
		
	}
	
	/**
     * Endpoint to drop a message at a specific node in the simulation.
     * 
     * @param nodeId The ID of the node where the message needs to be dropped.
     * @throws Exception If the message dropping operation fails.
     */
	@MessageMapping("/drop_message")
	private void dropMessage(String nodeId) throws Exception{
		if(! simulator.dropMessage(nodeId)) {
			throw new Exception("Dropping message didnt work");
		}
		
	}
	
	/**
     * Endpoint to alter the content of a message at a specific node in the simulation.
     * 
     * @param nodeId The ID of the node where the message content needs to be altered.
     * @throws Exception If the message altering operation fails.
     */
	@MessageMapping("/alter_message")
	private void alterMessage(String nodeId) throws Exception{
		if(! simulator.alterMessage(nodeId)) {
			throw new Exception("Altering message didnt work");
		}
		
	}
	
}
