package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.Controller;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.producermq.KafkaMessageProducer;

/**
 * 
 * @author HegdeSagar
 *
 */
public class Node implements INode {

	private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);
	
	private static final String MESSAGE_CONTENT_STRING = "Hello World!!";

	//private int simulationCount = 0;

	protected NodeId nodeId;

	protected boolean isLeader = false;

	protected volatile BlockingQueue<IMessage> messageQueue = null;

	protected IBroadcastStrategy strategy;

	private int N;

	private int F;

	private final Controller controller;
	
	private boolean isInterrupt = false;
	
	private final KafkaMessageProducer producer;
	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy, int _N, int _F, Controller controller, NodeId _nodeId, boolean isLeader)
			throws Exception {
		this.nodeId = _nodeId;
		_strategy.setNodeId(this.nodeId);
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();
		this.F = _F;
		this.N = _N;
		this.controller = controller;
		this.isLeader = isLeader;
		
		this.producer = new KafkaMessageProducer();
		
		strategy.setMQProducer(producer);

	}

	@Override
	public void run() {
		//simulationCount++; // TODO
		int noMessageCount = 0;
		if (isLeader) {
			strategy.leaderBroadcast(new MessageContent(MESSAGE_CONTENT_STRING)); // TODO this needs to be better
		}

		while (NetworkSimulator.getInstance().isSystemInSimulation()) {
			
			try {
				if (controller.isResetInProgress() && !isLeader()) { // Only non-leader nodes reset here

					//strategy = new AuthenticatedEchoBroadcastStrategy(N, F);
					//strategy.setNodeId(nodeId);
					strategy.reset();
					messageQueue.clear();// Added this to clear the queue once the message is delivered
					controller.decrementLatch();
					controller.awaitUntillLatch();
				}

				IMessage message = messageQueue.poll(1000+ NetworkSimulator.getInstance().getNetworkLatency(),
						TimeUnit.MILLISECONDS);
				 
				
				if(isInterrupt) {
					messageQueue.clear();
					strategy.reset();
				}
				if (message != null && !isInterrupt) {
					//check for tampering
					if(!MESSAGE_CONTENT_STRING.equals(message.getContent().toString())) {
						LOGGER.warn("Message TAMPERED Detected");
					}
					
					
					// Process the message
					if (strategy.executeStrategy(message)) {
						if (isLeader()) { // Leader initiates reset
							controller.initiateReset(N - 1);
							System.out.println(" The Message was delivered by node leader : " + nodeId);
							//System.out.println("Node :" + nodeId + " is elected as leader!!!.... and Simulation Count :"
							//		+ simulationCount);
							//strategy = new AuthenticatedEchoBroadcastStrategy(N, F);
							//strategy.setNodeId(nodeId);
							strategy.reset();
							messageQueue.clear();// Added this to clear the queue once the message is delivered
							controller.awaitLatchAndReset();
							BroadcastEvent event = new BroadcastEvent(nodeId, nodeId, MessageType.DELIVERED,
									NetworkSimulator.getInstance().getNodeName());
							producer.produce(event);
							//simulationCount++;
							// start broadcasting again
							strategy.leaderBroadcast(new MessageContent("Hello World!!"));
						}
					} 
				}else if(isLeader() && message == null){
					//The message is not delivered due to faulty nodes
					controller.initiateReset(N - 1);
					System.out.println(" The Message was NOT delivered by node leader : ");
					strategy.publishNotDelivered(message);
					strategy.reset();
					messageQueue.clear();// Added this to clear the queue once the message is delivered
					controller.awaitLatchAndReset();
					strategy.leaderBroadcast(new MessageContent("Hello World!!"));
				}
			}catch (InterruptedException e) {
				System.out.println("Interrupted : "+nodeId);
			} 
			catch (Exception e) {
				System.out.println("Exception Occurred here");
				e.printStackTrace();
			}
		}
		System.out.println(" Exiting node run method & simulation Count :" +nodeId);
		strategy.close(); // Close the Kafka connection
	}

	/*
	 * public BlockingQueue<IMessage> getMessageQueue() { return messageQueue; }
	 */
	@Override
	public synchronized void publishMessage(IMessage msg) throws InterruptedException {
		this.messageQueue.put(msg);
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public synchronized void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isLeader() {
		return isLeader;
	}


	@Override
	public void stop() {

	}

	@Override
	public void setInterrupt(boolean b) {
		
		this.isInterrupt = true;
		
	}

	@Override
	public void injectFlooding(boolean b) {
		strategy.flood();
	}

	@Override
	public void injectDropMessage(boolean b) {
		strategy.startDropping();
	}

	@Override
	public void injectMessageTampering(boolean b) {
		strategy.startMessageTamper();
	}
	

}