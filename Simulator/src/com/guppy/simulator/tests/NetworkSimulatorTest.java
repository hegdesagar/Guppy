package com.guppy.simulator.tests;

import com.guppy.simulator.core.NetworkSimulator;

public class NetworkSimulatorTest {

	public static void main(String[] args) {
		NetworkSimulator simulator = NetworkSimulator.getInstance();
		simulator.simulateNetwork(5); // simulate a network with 5 nodes
	}
	
}
