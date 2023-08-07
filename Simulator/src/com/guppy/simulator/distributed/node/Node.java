package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
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
public class Node extends AbstractNode implements INode{
	
	private Future<Boolean> futureTask;
	
	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy) {
		super(_strategy);
	}


	public void setLeader(boolean isLeader) {
		System.out.println("Node :" + nodeId + " is elected as leader!!!....");
		this.isLeader = isLeader;
		strategy.leaderBroadcast(new MessageContent("Hello World!!")); // TODO this needs to be better
	}

	public BlockingQueue<IMessage> getMessageQueue() {
		return messageQueue;
	}

	@Override
	public Boolean call() throws Exception {
		System.out.println("in Node Call method");
		while (!Thread.currentThread().isInterrupted()) {
			try {

				IMessage message = messageQueue.take();
				//If the message is delivered then inform the network
				if(MessageType.DELIVERED.equals(message.getType())){
					return true;
				}
				//execute the strategy for this message
				strategy.executeStrategy(message);

			} catch (InterruptedException e) {
	            // InterruptedException cleared interruption status of thread
	            // we should not call Thread.currentThread().interrupt() here
	            // just log the event and return from method
	            System.out.println("Thread was interrupted, Failed to complete operation");
	            NetworkSimulator.getInstance().setSystemInSimulation(false);
	            // rethrow InterruptedException to ensure enclosing method will handle it
	            //throw e;
	        } catch (Exception e) {
	            System.out.println("Non-interrupted exception: " + e.getMessage());
				NetworkSimulator.getInstance().setSystemInSimulation(false);
				return false;
	        }
		}
		return true;
	}
	
    public void setFutureTask(Future<Boolean> futureTask) {
        this.futureTask = futureTask;
    }

    public void stop() {
        // Cancel the future task, interrupting if running
        if (futureTask != null) {
            futureTask.cancel(true);
        }
    }

}