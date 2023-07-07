package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * 
 * @author HegdeSagar
 *
 */
public class Node extends AbstractNode implements Runnable {

	protected NodeId nodeId;

	private boolean isLeader = false;

	private BlockingQueue<IMessage> messageQueue = null;

	private IBroadcastStrategy strategy;

	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy) {
		super(_strategy);
	}

	@Override
	public void run() {

		while (true) {
			try {

				IMessage message = messageQueue.take();
				strategy.executeStrategy(message);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// TODO Handle the exception...
				System.out.println("Thred Interuppted Exception");
			}
		}

	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		System.out.println("Node :" + nodeId + " is elected as leader!!!....");
		this.isLeader = isLeader;
		strategy.leaderBroadcast(new MessageContent("Hello World!!")); //TODO this needs to be better
	}

	public BlockingQueue<IMessage> getMessageQueue() {
		return messageQueue;
	}

	protected String generateNodeId() {
		
		AtomicLong idCounter = new AtomicLong();

		String idVal = String.valueOf(idCounter.getAndIncrement());

		return new String(Constants.NODE_ID_PREFIX.concat(idVal));

	}

}