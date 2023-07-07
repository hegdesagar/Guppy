package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;

/*
 * 
 */
public abstract class AbstractNode implements INode {

	protected String nodeId;

	private boolean isLeader = false;

	private BlockingQueue<IMessage> messageQueue = null;

	private IBroadcastStrategy strategy;
	
	/*
	 * 
	 */
	protected AbstractNode(IBroadcastStrategy _strategy) {
		
		this.nodeId = generateNodeId();
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();

	}

	protected abstract String generateNodeId();
	
	public abstract BlockingQueue<IMessage> getMessageQueue();

}
