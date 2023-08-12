package com.guppy.simulator.distributed.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.Controller;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.producermq.KafkaMessageProducer;
//import com.guppy.simulator.rabbitmq.service.RabbitMQService;

/**
 * 
 * @author HegdeSagar
 *
 */
public class Node implements INode {

	private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

	private int simulationCount = 0;

	protected NodeId nodeId;

	protected boolean isLeader = false;

	protected volatile BlockingQueue<IMessage> messageQueue = null;

	protected IBroadcastStrategy strategy;

	private int N;

	private int F;

	private final Controller controller;

	// private static volatile RabbitMQService rabbitMQService;

	private final KafkaMessageProducer producer;

	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy, int _N, int _F, Controller controller, NodeId _nodeId, boolean isLeader)
			throws Exception {
		// this.nodeId = generateNodeId();
		this.nodeId = _nodeId;
		_strategy.setNodeId(this.nodeId);
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();
		this.F = _F;
		this.N = _N;
		this.controller = controller;
		this.producer = new KafkaMessageProducer();
		this.isLeader = isLeader;

	}

	@Override
	public void run() {

		simulationCount++; // TODO
		if (isLeader) {
			strategy.leaderBroadcast(new MessageContent("Hello World!!")); // TODO this needs to be better
		}

		while (NetworkSimulator.getInstance().isSystemInSimulation() && simulationCount < 2) {
			try {
				if (controller.isResetInProgress() && !isLeader()) { // Only non-leader nodes reset here

					strategy = new AuthenticatedEchoBroadcastStrategy(N, F);

					strategy.setNodeId(nodeId);
					messageQueue.clear();// Added this to clear the queue once the message is delivered
					controller.decrementLatch();
					controller.awaitUntillLatch();
				}

				IMessage message = messageQueue.poll(1000, TimeUnit.MILLISECONDS);
				if (message != null) {
					// Process the message
					if (strategy.executeStrategy(message)) {
						if (isLeader()) { // Leader initiates reset
							controller.initiateReset(N - 1);
							System.out.println(" The Message was delivered by node leader : " + nodeId);
							System.out.println("Node :" + nodeId + " is elected as leader!!!.... and Simulation Count :"
									+ simulationCount);
							strategy = new AuthenticatedEchoBroadcastStrategy(N, F);
							strategy.setNodeId(nodeId);
							messageQueue.clear();// Added this to clear the queue once the message is delivered
							controller.awaitLatchAndReset();
							// setLeader(true); // Start Broadcast again
							// Publlish the Delivered event to the queue
							BroadcastEvent deliverEvent = new BroadcastEvent(message.getSenderId(), nodeId,
									MessageType.DELIVERED, NetworkSimulator.getInstance().getNodeName());
							producer.produce(deliverEvent);

							simulationCount++;
							// start broadcasting again
							strategy.leaderBroadcast(new MessageContent("Hello World!!"));
						}
					} else {
						// broadcast the message to the RabbitMQ
						BroadcastEvent event = new BroadcastEvent(message.getSenderId(), nodeId, message.getType(),
								NetworkSimulator.getInstance().getNodeName());
						// Publish the event that a message has arrived
						producer.produce(event);
					}
				}
			} catch (Exception e) {
				System.out.println("Exception Occurred here");
				e.printStackTrace();
			}
		}
		System.out.println(" Exiting node run method & simulation Count :" + simulationCount);
		close(); // Close the Kafka connection
	}

	/*
	 * Sets a node as the leader and starts broadcasting message
	 */
	/*
	 * public void setLeader(boolean isLeader) { if (simulationCount < 2) {
	 * simulationCount++; // TODO System.out.println( "Node :" + nodeId +
	 * " is elected as leader!!!.... and Simulation Count :" + simulationCount);
	 * this.isLeader = isLeader; strategy.leaderBroadcast(new
	 * MessageContent("Hello World!!")); // TODO this needs to be better }
	 * 
	 * }
	 */

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

	public void close() {
		if (producer != null) {
			producer.close();
		}
	}

}