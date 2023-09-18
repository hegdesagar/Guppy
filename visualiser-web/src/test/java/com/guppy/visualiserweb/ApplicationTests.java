package com.guppy.visualiserweb;

import org.junit.Test;

import com.guppy.visualiserweb.controller.VisualiserController;
import com.guppy.visualiserweb.data.model.SimulationOptions;

public class ApplicationTests {

	@Test
	public void applicationRunTest() {

		VisualiserController controller = new VisualiserController();

		SimulationOptions options = new SimulationOptions();
		options.setNodes(5);
		options.setImplementation("AuthenticatedEchoBroadcastStrategy");
		options.setTimeline(10);
		options.setFaults(1);
		
		try {
			controller.simulate(options);
			Thread.sleep(10000);
			controller.stopSimulation();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

