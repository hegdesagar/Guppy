package com.guppy.simulator.core;

import java.util.ArrayList;
import java.util.List;

import com.guppy.simulator.common.typdef.NodeId;
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

	public ArrayList<NodeId> getNodeName();
	
}


/*while (system_in_Simulation) {
Boolean isDelivered = false;
try {
    isDelivered = futures.get(0).get(10, TimeUnit.SECONDS); // timeout of 10 seconds
    // If messages are delivered, reset the nodes and continue simulation
    if (isDelivered) {
        System.out.println("STARTING SIMULATION AGAIN");
        
        // Cancel the old futures
        for (Future<Boolean> future : futures) {
           // future.cancel(true); // mayInterruptIfRunning = true
        }
        futures.clear(); // clear the old futures
        
        // Reset the nodes and create new tasks
        for (INode node : nodeList) {
            //node.reset(); // Reset the state of the node for a new simulation
            Future<Boolean> future = service.submit(node); // submit a new task for the node
            futures.add(future); // add the new future to the list
        }
        simulator.electLeader(); // Elect a new leader if needed 
    }
}catch (InterruptedException | ExecutionException e) {
	System.out.println("Issue is here......");
    e.printStackTrace();
} catch (TimeoutException e) {
    System.out.println("Timeout occurred while waiting for message delivery.");
}
finally{
	System.out.println("Executing Finally Block.. NEED TO DO SOME CLEAN UP");
}
}*/