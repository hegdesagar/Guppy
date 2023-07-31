package com.guppy.visualiserweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.NetworkGraph;
import com.guppy.visualiserweb.data.model.Node;
import com.guppy.visualiserweb.data.model.NodeData;
import com.guppy.visualiserweb.data.model.SimulationOptions;

@Controller
public class VisualiserController {

    NetworkGraph graph = new NetworkGraph();

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/simulate")
    public void simulate(SimulationOptions options) throws Exception {
        System.out.println("In simulation:");

        Integer nodes = options.getNodes();  // Accessing the nodes
        String implementation = options.getImplementation(); // Accessing the implementation
        int timeline = options.getTimeline(); // Accessing the timeline

        
        //TODO remove these after testing
        System.out.println("Nodes: " + nodes);
        System.out.println("Implementation: " + implementation);
        System.out.println("Timeline: " + timeline);

        // Further processing based on nodes, implementation, and timeline
        // ...

        System.out.println("Returning from simulation:");

        while (true) {
            populateGraph();
            Thread.sleep(1000); // simulated delay of 1s
            // Sending the data whenever the NetworkGraph object changes
            template.convertAndSend("/topic/simulate_data", graph);
        }
    }

    @MessageMapping("/highlight")
    public void highlightNodes() throws Exception {
        // replace with your logic for determining which nodes to highlight
        List<String> nodesToHighlight = Arrays.asList("a");
        template.convertAndSend("/topic/highlight_nodes", nodesToHighlight);
    }

    private void populateGraph() {
        // Create nodes
        Node nodeA = new Node(new NodeData("a"));
        Node nodeB = new Node(new NodeData("b"));
        Node nodeC = new Node(new NodeData("d"));

        // Create edges
        Edge edgeAB = new Edge(new EdgeData("ab", 1, nodeA.getNodeData().getId(), nodeB.getNodeData().getId()));
        Edge edgeAC = new Edge(new EdgeData("ac", 1, nodeA.getNodeData().getId(), nodeC.getNodeData().getId()));
        Edge edgeBA = new Edge(new EdgeData("ba", 1, nodeB.getNodeData().getId(), nodeA.getNodeData().getId()));
        Edge edgeBC = new Edge(new EdgeData("bc", 1, nodeB.getNodeData().getId(), nodeC.getNodeData().getId()));
        Edge edgeCA = new Edge(new EdgeData("ca", 1, nodeC.getNodeData().getId(), nodeA.getNodeData().getId()));
        Edge edgeCB = new Edge(new EdgeData("cb", 1, nodeC.getNodeData().getId(), nodeB.getNodeData().getId()));

        List<Node> nodes = new ArrayList<Node>();
        List<Edge> edges = new ArrayList<Edge>();
        nodes.add(nodeA);
        nodes.add(nodeB);
        nodes.add(nodeC);

        edges.add(edgeAB);
        edges.add(edgeAC);
        edges.add(edgeBA);
        edges.add(edgeBC);
        edges.add(edgeCA);
        edges.add(edgeCB);

        Elements elements = new Elements(nodes, edges);

        graph.setElements(elements);
    }
}
