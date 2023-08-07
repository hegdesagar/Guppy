package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.NodeId;

/*
 * 
 */
public abstract class AbstractNode {
	
	private static final AtomicLong idCounter = new AtomicLong();

	protected NodeId nodeId;

	protected boolean isLeader = false;

	protected BlockingQueue<IMessage> messageQueue = null;

	protected IBroadcastStrategy strategy;

	/*
	 * 
	 */
	 AbstractNode(IBroadcastStrategy _strategy) {

		this.nodeId = generateNodeId();
		_strategy.setNodeId(this.nodeId);
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();

	}

	protected synchronized NodeId generateNodeId() {
		String idVal = String.valueOf(idCounter.getAndIncrement());
		return new NodeId(Constants.NODE_ID_PREFIX.concat(idVal));
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public IBroadcastStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(IBroadcastStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setMessageQueue(BlockingQueue<IMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}

}
