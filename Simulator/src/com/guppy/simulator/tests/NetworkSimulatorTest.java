package com.guppy.simulator.tests;


import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.guppy.simulator.core.ISimulator;
import com.guppy.simulator.core.NetworkSimulator;

public class NetworkSimulatorTest {

	@Test
	 public void SimulatorInjectFaultTest() {
		
		ISimulator simulator = NetworkSimulator.getInstance();
		String IMPLEMENTATION = "AuthenticatedEchoBroadcast";
		int nodes = 5;
		int faults = 2;
		
		try {
			Runnable t = new Runnable() {
				@Override
				public void run() {
					simulator.startSimulation(nodes,IMPLEMENTATION,faults);
				}
			};
			
			Thread th = new Thread(t);
			th.start();
			
			Thread.sleep(6000);
			System.out.println("Now will try to inject faults");
			boolean result = simulator.injectFault("node-1");
			assertTrue(result);
			
			//Stop the simulation
			simulator.stopSimulation();
			
		} catch (Exception e) {
			System.out.println("Some Error Occured");
			e.printStackTrace();
		} 
		
	}
	
	@Test
	 public void SimulatorInjectFaultTest1() {
		
		ISimulator simulator = NetworkSimulator.getInstance();
		int nodes = 5;
		int faults = 2;
		
		try {
			Runnable t = new Runnable() {
				@Override
				public void run() {
					simulator.startSimulation(nodes,"AuthenticatedDoubleEchoBroadcast",faults);
				}
			};
			
			Thread th = new Thread(t);
			th.start();
			
			Thread.sleep(6000);
			System.out.println("Now will try to inject faults");
			boolean result = simulator.injectFault("node-1");
			assertTrue(result);
			
			//Stop the simulation
			simulator.stopSimulation();
			
		} catch (Exception e) {
			System.out.println("Some Error Occured");
			e.printStackTrace();
		} 
		
	}
	
	@Test
	 public void SimulatorInjectFaultTest2() {
		
		ISimulator simulator = NetworkSimulator.getInstance();
		int nodes = 5;
		int faults = 2;
		
		try {
			Runnable t = new Runnable() {
				@Override
				public void run() {
					simulator.startSimulation(nodes,"BrachasBroadcast",faults);
				}
			};
			
			Thread th = new Thread(t);
			th.start();
			
			Thread.sleep(6000);
			System.out.println("Now will try to inject faults");
			boolean result = simulator.injectFault("node-1");
			assertTrue(result);
			
			//Stop the simulation
			simulator.stopSimulation();
			
		} catch (Exception e) {
			System.out.println("Some Error Occured");
			e.printStackTrace();
		} 
		
	}
}
