package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.NetworkSimulator;

/**
 * 
 * @author HegdeSagar
 *
 */
public class Node extends AbstractNode implements INode {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

	private Future<Boolean> futureTask;

	private int simulationCount = 0;

	//private boolean isStoped = true;

	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy) {
		super(_strategy);
	}

	/*
	 * Sets a node as the leader and starts broadcasting message
	 */
	public void setLeader(boolean isLeader) {
		System.out.println("Node :" + nodeId + " is elected as leader!!!....");
		this.isLeader = isLeader;
		strategy.setIsDelivered(false);
		strategy.leaderBroadcast(new MessageContent("Hello World!!")); // TODO this needs to be better
	}

	/*public BlockingQueue<IMessage> getMessageQueue() {
		return messageQueue;
	}*/
	@Override
	public synchronized void publishMessage(IMessage msg) throws InterruptedException {
		this.messageQueue.put(msg);
	}

	@Override
	public Boolean call() throws Exception {

		simulationCount++; // increase the simulation count
		System.out.println("in Node Call method simulation count::" + simulationCount);
		//isStoped = false;
		while (NetworkSimulator.getInstance().isSystemInSimulation()) {
			try {
				// If the message is delivered then inform the network
				if (strategy.isDelivered() && isLeader) {
					System.out.println("DELEIVERED>>>>>>>>>>>>>>>>>");
					reset();
					return true;
				}
				try{
					IMessage message = messageQueue.take();
					// execute the strategy for this message
					strategy.executeStrategy(message);
				}catch (InterruptedException e) {
					
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

	public void setFutureTask(Future<Boolean> futureTask) {
		this.futureTask = futureTask;
	}

	public void stop() {
		// Cancel the future task, interrupting if running
		if (futureTask != null) {
			futureTask.cancel(true);
		}
		//isStoped = true;
	}

	@Override
	public void reset() {
		messageQueue = new LinkedBlockingQueue<IMessage>();
		isLeader = false;
		//simulationCount = 0;
		strategy.setSentEcho(false);
		strategy.setIsDelivered(false);
		strategy.resetEchoMessages();
		//isStoped = true;

	}

	@Override
	public Boolean isDelivered() {
		
		return strategy.isDelivered();
	}

}