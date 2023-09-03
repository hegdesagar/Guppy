package com.guppy.simulator.tests;

import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;

public class NetworkSimulatorTest {

	public static void main(String[] args) {
		
		ISimulator simulator = NetworkSimulator.getInstance();
		try {
			//simulator.simulateNetwork(5,"AuthenticatedEchoBroadcast",1);
			Runnable t = new Runnable() {
				
				@Override
				public void run() {
					
					//simulator.startSimulation(5,"AuthenticatedEchoBroadcast",1);
					simulator.startSimulation(5,"CPABroadcastStrategy", 2);
					
				}
			};
			
			Thread th = new Thread(t);
			th.start();
			
			Thread.sleep(60000);
			System.out.println("NOw will try to inject faults");
			boolean result = simulator.injectFault("node-1");
			
			System.out.println("Is the node stopped : "+result);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Some Error Occured");
			e.printStackTrace();
		} 
		
	}
}
