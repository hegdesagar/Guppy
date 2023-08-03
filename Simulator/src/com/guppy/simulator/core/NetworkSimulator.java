package com.guppy.simulator.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.distributed.node.INode;
import com.guppy.simulator.distributed.node.Node;

public class NetworkSimulator {

	private static NetworkSimulator simulator;

	private List<INode> nodeList;

	// private IBroadcastStrategy broadcastStrategy;

	private boolean system_in_Simulation;

	//private Integer faults;

	ExecutorService service;

	private static Map<String, String> strategiesMap = new HashMap<String, String>();

	private NetworkSimulator() {

		nodeList = new ArrayList<INode>();
	}

	public static NetworkSimulator getInstance() {
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

		while (system_in_Simulation) {
			// create nodes based on strategy
			for (int i = 0; i < noOfNodes; i++) {
				// TODO faults needs to be calculated
				IBroadcastStrategy broadStrat = createObject(strategy, noOfNodes, noOfNodes);
				Node node = new Node(broadStrat);
				nodeList.add(node);

			}

			service = Executors.newFixedThreadPool(noOfNodes);

			List<Future<Boolean>> futures = new ArrayList<>();
			for (INode node : nodeList) {
				Future<Boolean> future = service.submit(node);
				futures.add(future);
			}

			Boolean isDelivered = futures.get(0).get();
			if (isDelivered) {
				// Start the simulation again
				System.out.println("The message was delivered and starting next batch of messages!!");
			}

		}

	}

	public void startSimulation(int noOfNodes, String strategy, Integer faults) {

		// Simulate the Network
		try {
			simulateNetwork(noOfNodes, strategy, faults);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void stopSimulation() {
		system_in_Simulation = false;
		service.shutdown();
	}

	public void electLeader() {
		// TODO elect a legitimate leader
		nodeList.get(0).setLeader(true);
	}

	public void injectFault() {
		// TODO Auto-generated method stub

	}

	private static void populateStrategies() {

		strategiesMap.put("AuthenticatedEchoBroadcast",
				"com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy");

	}

	private IBroadcastStrategy createObject(String strategyString, int noOfNodes, int faults) {
		String fullyQualifiedStrategyName = strategiesMap.get(strategyString);

		IBroadcastStrategy object = null;
		try {
			Class<?> classDefinition = Class.forName(fullyQualifiedStrategyName);
			Constructor<?> constructor = classDefinition.getConstructor(int.class, int.class);
			object = (IBroadcastStrategy) constructor.newInstance(noOfNodes, faults);
		} catch (InstantiationException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return object;
	}

}
