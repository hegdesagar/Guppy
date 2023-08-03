package com.guppy.simulator.core;

import java.util.List;

import com.guppy.simulator.distributed.node.INode;

public interface ISimulator {
	
	public void startSimulation();
	
	public void stopSimulation();
	
	public List<INode> getNodes();
	
	public void injectFault();

}
