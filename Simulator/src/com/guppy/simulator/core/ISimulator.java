package com.guppy.simulator.core;

import java.util.List;

import com.guppy.simulator.distributed.node.INode;

public interface ISimulator {
	
	public void startSimulation(int noOfNodes, String strategy, Integer faults);
	
	public void stopSimulation();
	
	public List<INode> getNodes();
	
	public boolean injectFault(String nodeId);
	
	public boolean configNetworkLatency(int millSec);
	
	public boolean selectLeader(String nodeId);

	public static NetworkSimulator getInstance() {
        return null;  // default implementation, will be overridden in actual class
    }

	public void electLeader();
	
	public boolean isSystemInSimulation();
	
	public void setSystemInSimulation(boolean flag);
	
}
