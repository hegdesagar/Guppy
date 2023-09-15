package com.guppy.visualiserweb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.guppy.visualiserweb.data.model.Edge;
import com.guppy.visualiserweb.data.model.EdgeData;
import com.guppy.visualiserweb.data.model.Elements;
import com.guppy.visualiserweb.data.model.NetworkGraph;
import com.guppy.visualiserweb.data.model.Node;
import com.guppy.visualiserweb.data.model.NodeData;

@SpringBootTest
public class VisualiserControllerTest {
	
	@Test
	private void populateGraph(boolean flg) {
		 NetworkGraph graph = new NetworkGraph();
		
		if(flg) {
		// Create nodes
		Node nodeA = new Node(new NodeData("a","leader"));
		Node nodeB = new Node(new NodeData("b",""));
		Node nodeC = new Node(new NodeData("d",""));

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
		
		}else {
			// Create nodes
			Node nodeA = new Node(new NodeData("a","leader"));
			Node nodeB = new Node(new NodeData("b",""));
			Node nodeC = new Node(new NodeData("c",""));

			// Create edges
			/*Edge edgeAB = new Edge(new EdgeData("ab", 1, nodeA.getNodeData().getId(), nodeB.getNodeData().getId()));
			Edge edgeAC = new Edge(new EdgeData("ac", 1, nodeA.getNodeData().getId(), nodeC.getNodeData().getId()));
			Edge edgeBA = new Edge(new EdgeData("ba", 1, nodeB.getNodeData().getId(), nodeA.getNodeData().getId()));
			Edge edgeBC = new Edge(new EdgeData("bc", 1, nodeB.getNodeData().getId(), nodeC.getNodeData().getId()));
			Edge edgeCA = new Edge(new EdgeData("ca", 1, nodeC.getNodeData().getId(), nodeA.getNodeData().getId()));
			Edge edgeCB = new Edge(new EdgeData("cb", 1, nodeC.getNodeData().getId(), nodeB.getNodeData().getId()));
*/
			List<Node> nodes = new ArrayList<Node>();
			List<Edge> edges = new ArrayList<Edge>();
			nodes.add(nodeA);
			nodes.add(nodeB);
			nodes.add(nodeC);

			/*edges.add(edgeAB);
			edges.add(edgeAC);
			edges.add(edgeBA);
			edges.add(edgeBC);
			edges.add(edgeCA);
			edges.add(edgeCB);*/

			Elements elements = new Elements(nodes, edges);

			graph.setElements(elements);
		}

		/*
		 * // Add nodes to the graph graph.addNode(nodeA); graph.addNode(nodeB);
		 * graph.addNode(nodeC);
		 * 
		 * // Add edges to the graph graph.addEdge(edgeAB); graph.addEdge(edgeAC);
		 * graph.addEdge(edgeBA); graph.addEdge(edgeBC); graph.addEdge(edgeCA);
		 * graph.addEdge(edgeCB);
		 */
	}

}
