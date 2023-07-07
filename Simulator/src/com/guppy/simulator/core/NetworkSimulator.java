package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;

import com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy;
import com.guppy.simulator.distributed.node.INode;
import com.guppy.simulator.distributed.node.Node;

public class NetworkSimulator {

	private static NetworkSimulator simulator;

	private List<INode> nodeList;

	private NetworkSimulator() {

		nodeList = new ArrayList<INode>();
	}

	public static NetworkSimulator getInstance() {
		if (simulator == null) {
			simulator = new NetworkSimulator();
		}
		return simulator;
	}

	public List<INode> getNodes() {
		return nodeList;
	}

	public void simulateNetwork(int noOfNodes) {
		for (int i = 0; i < noOfNodes; i++) {
			Node node = new Node(new AuthenticatedEchoBroadcastStrategy(noOfNodes, 1));// TODO faults needs to be
																								// calculated
			nodeList.add(node);
			Thread thread = new Thread(node);
			thread.start();
		}

		nodeList.get(0).setLeader(true);
	}

}
