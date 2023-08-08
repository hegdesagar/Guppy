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
		system_in_Simulation = true;
		
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
		//System.out.println("IN BACKEND SIMULATION");
		//System.out.println("Nodes :: "+ noOfNodes);
		//TODO remove these hardcoaded values
		strategy ="AuthenticatedEchoBroadcast";
		faults = 1;
		//TODO end
		service = Executors.newFixedThreadPool(noOfNodes);
		//System.out.println("in Simulated Network :: Started Simulation");
		List<Future<Boolean>> futures = new ArrayList<>();
		// create nodes based on strategy
		for (int i = 0; i < noOfNodes; i++) {
			// Create an object of the strategy we are going to use
			IBroadcastStrategy broadStrat = createObject(strategy, noOfNodes, faults);
			//Create a node based on this strategy and add it to the list of nodes
			Node node = new Node(broadStrat);
			nodeList.add(node);
			//Add this future to inject faults later on
			Future<Boolean> future = service.submit(node);
			node.setFutureTask(future);
			futures.add(future);

		}
		simulator.electLeader();//Elect the leader
		while (system_in_Simulation) {
		    Boolean isDelivered = false;
		    try {
		        isDelivered = futures.get(0).get(10, TimeUnit.SECONDS); // timeout of 10 seconds
		        // If messages are delivered, reset the nodes and continue simulation
		        if (isDelivered) {
		            System.out.println("STARTING SIMULATION AGAIN");
		            
		            // Cancel the old futures
		            for (Future<Boolean> future : futures) {
		               // future.cancel(true); // mayInterruptIfRunning = true
		            }
		            futures.clear(); // clear the old futures
		            
		            // Reset the nodes and create new tasks
		            for (INode node : nodeList) {
		                //node.reset(); // Reset the state of the node for a new simulation
		                Future<Boolean> future = service.submit(node); // submit a new task for the node
		                futures.add(future); // add the new future to the list
		            }
		            simulator.electLeader(); // Elect a new leader if needed 
		        }
		    }catch (InterruptedException | ExecutionException e) {
		    	System.out.println("Issue is here......");
		        e.printStackTrace();
		    } catch (TimeoutException e) {
		        System.out.println("Timeout occurred while waiting for message delivery.");
		    }
		    finally{
		    	System.out.println("Executing Finally Block.. NEED TO DO SOME CLEAN UP");
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
		//System.out.println("no of nodes created :+"+nodeList.size());
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
