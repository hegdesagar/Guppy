package com.guppy.simulator.distributed.node;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;

/**
 * 
 * @author HegdeSagar
 *
 */
public class Node implements Runnable , INode{
	
	
	
	protected String nodeId;
	
	private boolean isLeader = false;
	
	private BlockingQueue<IMessage> messageQueue = null;
	
	private IBroadcastStrategy strategy;
	
	/*
	 * Constructor for node initialization
	 */
	public Node(String _nodeId, IBroadcastStrategy _strategy){
		this.nodeId = _nodeId;
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>() {

		};
	}
	

	@Override
	public void run() {
		
		while(true) {
			try {

				IMessage message = messageQueue.take();
				strategy.executeStrategy(message);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//TODO Handle the exception...
				System.out.println("Thred Interuppted Exception");
			}
		}
			
	}
	
	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		System.out.println("Node :"+nodeId+" is elected as leader!!!....");
		this.isLeader = isLeader;
		strategy.leaderBroadcast("Hello World!!");
	}
	
	public BlockingQueue<IMessage> getMessageQueue() {
		return messageQueue;
	}
	

}