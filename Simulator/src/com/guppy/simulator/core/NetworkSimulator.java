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
package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.reflections.Reflections;

import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.broadcast.strategy.annotation.BroadCastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.distributed.node.INode;
import com.guppy.simulator.distributed.node.Node;

/**
 * Represents a simulator for network operations and behaviors. 
 * The `NetworkSimulator` provides functionalities to simulate 
 * different network scenarios and conditions, allowing for testing
 * and analysis of network-based systems.
 *
 * <p>This class extends the {@link AbstractNetworkSimulator} to 
 * offer concrete implementations of certain simulation behaviors 
 * and also implements the {@link ISimulator} interface to ensure 
 * adherence to the expected simulation protocol and is Singleton.</p>
 *
 * <p>As a final class, it cannot be subclassed, ensuring the integrity 
 * of the simulation behavior defined in this class.</p>
 *
 *
 *
 * @author HegdeSagar
 * @see AbstractNetworkSimulator
 * @see ISimulator
 * @version 1.0
 */
public final class NetworkSimulator extends AbstractNetworkSimulator implements ISimulator {

	private static final AtomicLong idCounter = new AtomicLong();

	private static NetworkSimulator simulator;

	public volatile boolean system_in_Simulation = false;

	private ExecutorService service;

	private final ArrayList<NodeId> nodeName;

	private volatile Integer networkLatency = 1;

	private Integer faultNodesInjected = 0;

	/**
     * Private constructor to ensure the singleton pattern for the NetworkSimulator.
     * Initializes the nodeList, nodeName, system_in_Simulation, and sets a default network latency.
     */
	private NetworkSimulator() {
		nodeList = new ArrayList<INode>();
		system_in_Simulation = true;
		nodeName = new ArrayList<NodeId>();
		this.networkLatency = 100;
	}

	/**
     * Retrieves the sole instance of the NetworkSimulator. If the instance does not exist,
     * it initializes and returns it. This method also ensures that the available broadcast
     * strategies are populated before returning the simulator instance.
     *
     * @return the single instance of the NetworkSimulator.
     */
	public static ISimulator getInstance() {
		if (simulator == null) {
			simulator = new NetworkSimulator();
		}
		return simulator;
	}

	/*
	 * Get the nodes listed in the network
	 */
	public List<INode> getNodes() {
		return nodeList;
	}

	/**
	 * Simulates a distributed network based on the specified number of nodes, broadcasting strategy, and number of faults.
	 * <p>
	 * This method creates a thread pool using the Executor framework, where each node in the network is represented by a Java thread. 
	 * The method follows a special sequencing by first creating the leader node but not immediately starting it to avoid premature broadcasting.
	 * After the other nodes are constructed and started, the leader node is finally initiated to possibly trigger the broadcasting process.
	 * </p>
	 * 
	 * @param noOfNodes The number of nodes to be simulated in the network.
	 * @param strategy The broadcasting strategy to be employed for the nodes.
	 * @param faults The number of faults, if any, to be injected during simulation.
	 * 
	 * @throws Exception If an error occurs during the node creation, strategy assignment, or thread submission process.
	 */
	private void simulateNetwork(int noOfNodes, String strategy, Integer faults) throws Exception {

		service = Executors.newFixedThreadPool(noOfNodes, daemonThreadFactory);

		Controller controller = new Controller();
		
		// First create the leader node and dont submit it as it will trigger the broadcast
		NodeId leaderNodeId = generateNodeId();
		IBroadcastStrategy leaderBroadStrat = getStrategyObject(strategy,noOfNodes, faults,leaderNodeId);
		if(leaderBroadStrat==null) {
			return;
		}
		Node leaderNode = new Node(leaderBroadStrat, noOfNodes, faults, controller, leaderNodeId, true);
		nodeList.add(leaderNode);
		nodeName.add(leaderNodeId);

		// create other nodes based on strategy
		try {
			for (int i = 0; i < noOfNodes - 1; i++) {
				// Create a node based on this strategy and add it to the list of nodes
				NodeId nodeId = generateNodeId();
				// Create an object of the strategy we are going to use
				IBroadcastStrategy broadStrat = getStrategyObject(strategy,noOfNodes, faults,nodeId);
				if(broadStrat==null) {
					return;
				}
				Node node = new Node(broadStrat, noOfNodes, faults, controller, nodeId, false);
				nodeList.add(node);
				nodeName.add(nodeId);
				service.submit(node);
			}

			// now submit the leader node to start the broadcasting
			service.submit(leaderNode);

		} catch (Exception e) {
			service.shutdown();
			system_in_Simulation = false;
		}
	}

	/**
	 * Custom thread factory to create daemon threads for the network simulation.
	 * <p>
	 * Daemon threads are background threads that do not prevent the JVM from exiting 
	 * when the main execution thread completes, making them ideal for simulation purposes.
	 * </p>
	 */
	ThreadFactory daemonThreadFactory = new ThreadFactory() {
		private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

		 /**
	     * Creates a new daemon thread for the simulation.
	     *
	     * @param r The runnable task that the thread will execute.
	     * @return A new daemon thread.
	     */	
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = defaultFactory.newThread(r);
			thread.setDaemon(true);
			return thread;
		}
	};

	/*
	 * @inheritDoc 
	 */
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

	/*
	 * @inheritDoc 
	 */
	@Override
	public void stopSimulation() {
		system_in_Simulation = false;
		service.shutdown();
	}

	/*
	 * Elects a leader.
	 * TODO : this methods needs to be implemented in future
	 */
	public void electLeader() {
		// TODO elect a legitimate leader
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public boolean injectFault(String nodeId) {
		// Iterate over all the node and if found interrupt the node
		for (INode node : nodeList) {
			if (node.getNodeId().getId().equals(nodeId)) {
				node.setInterrupt(true);
				this.faultNodesInjected++;
				return true;
			}
		}
		return false;
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public boolean configNetworkLatency(int millSec) {
		return false;
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public boolean selectLeader(String nodeId) {
		return false;
	}

	/*
	 * Return true if the system is in Simulation , else false
	 */
	public boolean isSystemInSimulation() {
		return system_in_Simulation;
	}

	/*
	 * Set the boolean flag in simulation is in progress or not
	 */
	public void setSystemInSimulation(boolean flag) {
		system_in_Simulation = flag;
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public ArrayList<NodeId> getNodeName() {
		return nodeName;
	}

	/*
	 * Generates nodeid
	 */
	protected synchronized NodeId generateNodeId() {
		String idVal = String.valueOf(idCounter.getAndIncrement());
		return new NodeId(Constants.NODE_ID_PREFIX.concat(idVal));
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public void introduceLatencyInNetwork(int latency) {
		this.networkLatency = latency;
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public synchronized Integer getNetworkLatency() {
		return networkLatency;
	}

	/*
	 * @inheritDoc 
	 */
	@Override
	public int getNoOfFaultNodesInjected() {
		return faultNodesInjected;
	}

	/*
	 * @inheritDoc 
	 */
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

	/*
	 * @inheritDoc 
	 */
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

	/*
	 * @inheritDoc 
	 */
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
	
	/*
	 * Return the strategy object for the {@link IBroadcastStrategy} implementation
	 */
	private IBroadcastStrategy getStrategyObject(String implementation,int _N, int _f, NodeId nodeId) {
	    Reflections reflections = new Reflections("com.guppy.simulator.broadcast.strategy");
	    Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(BroadCastStrategy.class);

	    for (Class<?> clazz : annotatedClasses) {
	        BroadCastStrategy annotation = clazz.getAnnotation(BroadCastStrategy.class);
	        if (annotation.value().equals(implementation)) {
	            try {
	                // Check if the class implements IBroadcastStrategy
	                if(IBroadcastStrategy.class.isAssignableFrom(clazz)) {
	                	return (IBroadcastStrategy) clazz.getDeclaredConstructor(int.class, int.class, NodeId.class).newInstance(_N, _f, nodeId);
	                } else {
	                    throw new RuntimeException("Class " + clazz.getName() + " does not implement IBroadcastStrategy");
	                }
	            } catch (Exception e) {
	                throw new RuntimeException("Failed to instantiate class " + clazz.getName(), e);
	            }
	        }
	    }
	    return null; 
	}
}
