package com.guppy.simulator.tests;

import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;

public class NetworkSimulatorTest {

	public static void main(String[] args) {
		
		ISimulator simulator = NetworkSimulator.getInstance();
		try {
			//simulator.simulateNetwork(5,"AuthenticatedEchoBroadcast",1);
			simulator.startSimulation(5,"AuthenticatedEchoBroadcast",1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Some Error Occured");
			e.printStackTrace();
		} // simulate a network with 5 nodes
		
		//Start the simulation
		
		//fault some nodes
		//simulator.getInstance().getNodes().get(1).injectFault();
		//simulator.getInstance().getNodes().get(2).injectFault();
		//simulator.getInstance().getNodes().get(3).injectFault();
		//simulator.getInstance().getNodes().get(4).injectFault();
		
		
		
		
	}
	
}
