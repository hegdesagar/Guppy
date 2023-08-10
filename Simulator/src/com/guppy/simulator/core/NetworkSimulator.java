package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.distributed.node.INode;
import com.guppy.simulator.distributed.node.Node;

public final class NetworkSimulator extends AbstractNetworkSimulator implements ISimulator {

	private static final AtomicLong idCounter = new AtomicLong();
	
	private static NetworkSimulator simulator;

	public volatile boolean system_in_Simulation = false;

	private ExecutorService service;
	
	private final ArrayList<NodeId> nodeName;

	private NetworkSimulator() {
		nodeList = new ArrayList<INode>();
		system_in_Simulation = true;
		nodeName = new ArrayList<NodeId>();
	}

	public static ISimulator getInstance() {
		populateStrategies();
		if (simulator == null) {
			simulator = new NetworkSimulator();
		}
		return simulator;
	}

	public List<INode> getNodes() {
		return nodeList;
	}

	private void simulateNetwork(int noOfNodes, String strategy, Integer faults) throws Exception {

		// TODO remove these hardcoded values
		strategy = "AuthenticatedEchoBroadcast";
		faults = 2;
		// TODO end

		// List to hold all the Threads
		List<Thread> threads = new ArrayList<>();
		Controller controller = new Controller();
		// create nodes based on strategy
		try {
			for (int i = 0; i < noOfNodes; i++) {
				// Create an object of the strategy we are going to use
				IBroadcastStrategy broadStrat = new AuthenticatedEchoBroadcastStrategy(noOfNodes, faults);
				// Create a node based on this strategy and add it to the list of nodes
				NodeId nodeId = generateNodeId();
				Node node = new Node(broadStrat,noOfNodes, faults, controller, nodeId);
				nodeList.add(node);
				nodeName.add(nodeId);
				// Create a new Thread for each node and start it
				Thread nodeThread = new Thread(node);
				nodeThread.setDaemon(true);
				nodeThread.start();
				// Add the Thread to the list
				threads.add(nodeThread);
			}

			simulator.electLeader(); // Elect the leader
			long count =50;
			while (count>0) {
				Thread.sleep(100);
				// Keep running
				count--;
			}
			system_in_Simulation= false;
		} finally {
			// Optional: If you need to stop the Threads once the simulation is done
			for (Thread thread : threads) {
				thread.interrupt();
			}
			system_in_Simulation = false;
		}
	}


	@Override
	public void startSimulation(int noOfNodes, String strategy, Integer faults) {

		system_in_Simulation = true;
		// Simulate the Network
		try {
			simulateNetwork(noOfNodes, strategy, faults);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void stopSimulation() {
		system_in_Simulation = false;
		service.shutdown();
	}

	public void electLeader() {
		// TODO elect a legitimate leader
		// System.out.println("no of nodes created :+"+nodeList.size());
		nodeList.get(0).setLeader(true);
	}

	@Override
	public boolean injectFault(String nodeId) {
		for (INode node : nodeList) {
			if (node.getNodeId().equals(nodeId)) {
				// node.stop();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean configNetworkLatency(int millSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectLeader(String nodeId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSystemInSimulation() {
		return system_in_Simulation;
	}

	public void setSystemInSimulation(boolean flag) {
		system_in_Simulation = flag;
	}

	@Override
	public ArrayList<NodeId> getNodeName() {
		return nodeName;
	}
	
	protected synchronized NodeId generateNodeId() {
		String idVal = String.valueOf(idCounter.getAndIncrement());
		return new NodeId(Constants.NODE_ID_PREFIX.concat(idVal));
	}

}
