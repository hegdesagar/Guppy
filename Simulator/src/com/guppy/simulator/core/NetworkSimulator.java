package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.distributed.node.INode;
import com.guppy.simulator.distributed.node.Node;

public final class NetworkSimulator extends  AbstractNetworkSimulator implements ISimulator{

	private static NetworkSimulator simulator;

	public  boolean system_in_Simulation = false;

	private ExecutorService service;


	private NetworkSimulator() {

		nodeList = new ArrayList<INode>();
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
		System.out.println("IN BACKEND SIMULATION");
		service = Executors.newFixedThreadPool(noOfNodes);
		System.out.println("in Simulated Network :: Started Simulation");
		// create nodes based on strategy
		for (int i = 0; i < noOfNodes; i++) {
			// TODO faults needs to be calculated
			IBroadcastStrategy broadStrat = createObject(strategy, noOfNodes, noOfNodes);
			Node node = new Node(broadStrat);
			nodeList.add(node);
			//Add this future to inject faults later on
			Future<Boolean> future = service.submit(node);
			node.setFutureTask(future);

		}
		simulator.electLeader();//Elect the leader
		while (system_in_Simulation) {
			List<Future<Boolean>> futures = new ArrayList<>();
			for (INode node : nodeList) {
				Future<Boolean> future = service.submit(node);
				futures.add(future);
			}

			Boolean isDelivered = null;
			try {
			    isDelivered = futures.get(0).get(10, TimeUnit.SECONDS); // timeout of 5 seconds
			    if (isDelivered) {
			        // Start the simulation again
			        System.out.println("The message was delivered and starting next batch of messages!!");
			    }
			} catch (InterruptedException | ExecutionException e) {
			    e.printStackTrace();
			} catch (TimeoutException e) {
			    System.out.println("Timeout occurred while waiting for message delivery.");
			}finally {
				//stop all the threads
			}
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
		System.out.println("no of nodes created :+"+nodeList.size());
		nodeList.get(0).setLeader(true);
	}
	


	@Override
	public boolean injectFault(String nodeId) {
	    for (INode node : nodeList) {
	        if (node.getNodeId().equals(nodeId)) {
	            node.stop();
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

}
