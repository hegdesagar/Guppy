package com.guppy.simulator.distributed.node;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.NodeId;

public interface INode extends  Runnable {

	//BlockingQueue<IMessage> getMessageQueue();
	
	void publishMessage(IMessage msg) throws InterruptedException;

//	void setLeader(boolean isLeader);
	
//	public void stop();
	
	public NodeId getNodeId();
	
	public boolean isLeader();

//	void reset();

//	Boolean isDelivered();


}


/*	@Override
public Boolean call() throws Exception {

	simulationCount++; // increase the simulation count
	System.out.println("in Node Call method simulation count::" + simulationCount);
	// isStoped = false;
	while (NetworkSimulator.getInstance().isSystemInSimulation()) {
		try {
			// If the message is delivered then inform the network
			if (strategy.isDelivered() && isLeader) {
				System.out.println("DELEIVERED>>>>>>>>>>>>>>>>>");
				reset();
				return true;
			}
			try {
				IMessage message = messageQueue.take();
				// execute the strategy for this message
				strategy.executeStrategy(message);
			} catch (InterruptedException e) {

				LOGGER.info("Node : {}  Intrrupted , But its okay", nodeId);
			}

		} catch (InterruptedException e) {
			// InterruptedException cleared interruption status of thread
			// we should not call Thread.currentThread().interrupt() here
			// just log the event and return from method
			System.out.println("Thread was interrupted, Failed to complete operation");
			e.printStackTrace();
			NetworkSimulator.getInstance().setSystemInSimulation(false);
			// rethrow InterruptedException to ensure enclosing method will handle it
			// throw e;
			return false;
		} catch (Exception e) {
			System.out.println("Non-interrupted exception: " + e.getMessage());
			NetworkSimulator.getInstance().setSystemInSimulation(false);
			return false;
		}
	}
	return false;
}
*/
/*	public void setFutureTask(Future<Boolean> futureTask) {
	this.futureTask = futureTask;
}

public void stop() {
	// Cancel the future task, interrupting if running
	if (futureTask != null) {
		futureTask.cancel(true);
	}
	// isStoped = true;
}
*/
