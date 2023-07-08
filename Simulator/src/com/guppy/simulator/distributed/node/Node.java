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
public class Node extends AbstractNode {

	private static final AtomicLong idCounter = new AtomicLong();

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
		strategy.leaderBroadcast(new MessageContent("Hello World!!")); // TODO this needs to be better
	}

	public BlockingQueue<IMessage> getMessageQueue() {
		return messageQueue;
	}

	protected synchronized NodeId generateNodeId() {
		String idVal = String.valueOf(idCounter.getAndIncrement());
		return new NodeId(Constants.NODE_ID_PREFIX.concat(idVal));
	}

	@Override
	public void injectFault() {
		boolean flag = true;
		synchronized (this) {
			try {
				while (flag) {
					this.wait();
				}
			} catch (InterruptedException e) {
				// Handle the exception
				System.out.println("Interrupted exeception...");
				Thread.currentThread().interrupt();
			}
		}
	}

}