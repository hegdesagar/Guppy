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

import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.distributed.node.INode;

/**
 * Represents an interface for the simulator which provides methods to control,
 * monitor, and modify the network simulation environment.
 */
public interface ISimulator {
	
	/**
	 * Starts a simulation with the given parameters.
	 * 
	 * @param noOfNodes The number of nodes in the simulation.
	 * @param strategy The chosen strategy for the simulation.
	 * @param faults The number of acceptable faults.
	 */
	public void startSimulation(int noOfNodes, String strategy, Integer faults);
	
	/**
	 * Stops the current simulation.
	 */
	public void stopSimulation();

	/**
	 * Retrieves all nodes present in the simulation.
	 * 
	 * @return A list of nodes.
	 */
	public List<INode> getNodes();

	/**
	 * Injects a fault into a specific node.
	 * 
	 * @param nodeId The ID of the node to inject fault into.
	 * @return True if the fault injection was successful, false otherwise.
	 */
	public boolean injectFault(String nodeId);

	/**
	 * Configures the network latency.
	 * 
	 * @param millSec The latency in milliseconds.
	 * @return True if the configuration was successful, false otherwise.
	 */
	public boolean configNetworkLatency(int millSec);

	/**
	 * Selects a leader node based on a specific node ID.
	 * 
	 * @param nodeId The ID of the node to be selected as leader.
	 * @return True if the leader selection was successful, false otherwise.
	 */
	public boolean selectLeader(String nodeId);

	/**
	 * Initiates a leader election process.
	 */
	public void electLeader();

	/**
	 * Checks if the system is currently in a simulation.
	 * 
	 * @return True if the system is in simulation, false otherwise.
	 */
	public boolean isSystemInSimulation();

	/**
	 * Sets the simulation state for the system.
	 * 
	 * @param flag The desired state of the simulation. True to start, false to stop.
	 */
	public void setSystemInSimulation(boolean flag);

	/**
	 * Retrieves the names of nodes present in the simulation.
	 * 
	 * @return An array list containing node IDs.
	 */
	public ArrayList<NodeId> getNodeName();

	/**
	 * Introduces a specific latency into the network.
	 * 
	 * @param timeline The timeline in milliseconds.
	 */
	public void introduceLatencyInNetwork(int timeline);

	/**
	 * Gets the current network latency.
	 * 
	 * @return The current network latency in milliseconds.
	 */
	public Integer getNetworkLatency();

	/**
	 * Retrieves the number of nodes that have had faults injected.
	 * 
	 * @return The count of nodes with injected faults.
	 */
	public int getNoOfFaultNodesInjected();

	/**
	 * Initiates flooding of a specific node.
	 * 
	 * @param nodeId The ID of the node to flood.
	 * @return True if the flooding was successful, false otherwise.
	 */
	public boolean flood(String nodeId);

	/**
	 * Drops messages for a specific node.
	 * 
	 * @param nodeId The ID of the node to drop messages for.
	 * @return True if the operation was successful, false otherwise.
	 */
	public boolean dropMessage(String nodeId);

	/**
	 * Alters messages for a specific node.
	 * 
	 * @param nodeId The ID of the node to alter messages for.
	 * @return True if the operation was successful, false otherwise.
	 */
	public boolean alterMessage(String nodeId);

	/**
	 * Provides a default implementation to retrieve an instance of the network simulator.
	 * This method should be overridden in the actual class implementation.
	 * 
	 * @return An instance of the NetworkSimulator or null if not overridden.
	 */
	public static NetworkSimulator getInstance() {
        return null;  
    }
}

