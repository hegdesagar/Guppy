package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.typdef.NodeId;

/*
 * 
 */
public abstract class AbstractNode implements INode {

	protected NodeId nodeId;

	protected boolean isLeader = false;

	protected BlockingQueue<IMessage> messageQueue = null;

	protected IBroadcastStrategy strategy;
	
	/*
	 * 
	 */
	protected AbstractNode(IBroadcastStrategy _strategy) {
		
		this.nodeId = generateNodeId();
		_strategy.setNodeId(this.nodeId);
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();

	}

	protected abstract NodeId generateNodeId();
	
	public abstract BlockingQueue<IMessage> getMessageQueue();
	
	public abstract void setLeader(boolean isLeader);
	
	public abstract void injectFault();

}
