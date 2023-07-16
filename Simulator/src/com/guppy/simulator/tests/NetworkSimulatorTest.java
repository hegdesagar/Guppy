package com.guppy.simulator.tests;

import com.guppy.simulator.core.NetworkSimulator;

public class NetworkSimulatorTest {

	public static void main(String[] args) {
		NetworkSimulator simulator = NetworkSimulator.getInstance();
		simulator.simulateNetwork(5); // simulate a network with 5 nodes
		
		//Start the simulation
		simulator.startSimulation();
		
		//fault some nodes
		//simulator.getInstance().getNodes().get(1).injectFault();
		//simulator.getInstance().getNodes().get(2).injectFault();
		//simulator.getInstance().getNodes().get(3).injectFault();
		//simulator.getInstance().getNodes().get(4).injectFault();
		
		simulator.electLeader();
		
		
	}
	
}
