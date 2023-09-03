package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
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

	private volatile Integer networkLatency = 1;

	private Integer faultNodesInjected = 0;

	// private Map<NodeId, Future<?>> futureList = new HashMap<>();

	private NetworkSimulator() {
		nodeList = new ArrayList<INode>();
		system_in_Simulation = true;
		nodeName = new ArrayList<NodeId>();
		this.networkLatency = 100;
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

		service = Executors.newFixedThreadPool(noOfNodes, daemonThreadFactory);

		// TODO remove these hardcoded values
		//strategy = "AuthenticatedEchoBroadcast";
		faults = 2;
		// TODO end

		// List to hold all the Threads
		// List<Thread> threads = new ArrayList<>();
		Controller controller = new Controller();

		// First create the leader node and dont submit it as it will trigger the
		// broadcast
		NodeId leaderNodeId = generateNodeId();
		IBroadcastStrategy leaderBroadStrat = new AuthenticatedEchoBroadcastStrategy(noOfNodes, faults,leaderNodeId);
		//IBroadcastStrategy leaderBroadStrat = new CPABroadcastStrategy(noOfNodes, faults,leaderNodeId);
		Node leaderNode = new Node(leaderBroadStrat, noOfNodes, faults, controller, leaderNodeId, true);
		nodeList.add(leaderNode);
		nodeName.add(leaderNodeId);

		// create other nodes based on strategy
		try {
			for (int i = 0; i < noOfNodes - 1; i++) {
				// Create a node based on this strategy and add it to the list of nodes
				NodeId nodeId = generateNodeId();
				// Create an object of the strategy we are going to use
				//IBroadcastStrategy broadStrat = new CPABroadcastStrategy(noOfNodes, faults,nodeId);
				IBroadcastStrategy broadStrat = new AuthenticatedEchoBroadcastStrategy(noOfNodes, faults,nodeId);
				Node node = new Node(broadStrat, noOfNodes, faults, controller, nodeId, false);
				nodeList.add(node);
				nodeName.add(nodeId);
				service.submit(node);
				// futureList.put(nodeId, nodeFuture);
			}

			// now submit the leader node to start the broadcasting
			service.submit(leaderNode);

		} catch (Exception e) {
			service.shutdown();
			system_in_Simulation = false;
		}
	}

	ThreadFactory daemonThreadFactory = new ThreadFactory() {
		private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = defaultFactory.newThread(r);
			thread.setDaemon(true);
			return thread;
		}
	};

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
		// nodeList.get(0).setLeader(true);
	}

	@Override
	public boolean injectFault(String nodeId) {
		// Iterate over all the node and if found interrupt the node
		/*
		 * for(Entry<NodeId, Future<?>> entry : futureList.entrySet()) {
		 * if(entry.getKey().getId().equals(nodeId)) { entry.getValue().cancel(true);
		 * return true; } }
		 */
		for (INode node : nodeList) {
			if (node.getNodeId().getId().equals(nodeId)) {
				node.setInterrupt(true);
				this.faultNodesInjected++;
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

	@Override
	public void introduceLatencyInNetwork(int latency) {
		this.networkLatency = latency;
	}

	@Override
	public synchronized Integer getNetworkLatency() {
		return networkLatency;
	}

	@Override
	public int getNoOfFaultNodesInjected() {
		// TODO Auto-generated method stub
		return faultNodesInjected;
	}

	@Override
	public boolean flood(String nodeId) {
		for (INode node : nodeList) {
			if (node.getNodeId().getId().equals(nodeId)) {
				node.injectFlooding(true);
				this.faultNodesInjected++;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean dropMessage(String nodeId) {
		for (INode node : nodeList) {
			if (node.getNodeId().getId().equals(nodeId)) {
				node.injectDropMessage(true);
				this.faultNodesInjected++;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean alterMessage(String nodeId) {
		for (INode node : nodeList) {
			if (node.getNodeId().getId().equals(nodeId)) {
				node.injectMessageTampering(true);
				this.faultNodesInjected++;
				return true;
			}
		}
		return false;
	}

}
