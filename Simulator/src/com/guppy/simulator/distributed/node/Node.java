package com.guppy.simulator.distributed.node;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.strategy.AuthenticatedEchoBroadcastStrategy;
import com.guppy.simulator.broadcast.strategy.IBroadcastStrategy;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.Controller;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.rabbitmq.service.RabbitMQService;

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
	
	private static volatile RabbitMQService rabbitMQService;
	
	/*
	 * Constructor for node initialization
	 */
	public Node(IBroadcastStrategy _strategy, int _N, int _F, Controller controller, NodeId _nodeId) throws Exception {
		//this.nodeId = generateNodeId();
		this.nodeId = _nodeId;
		_strategy.setNodeId(this.nodeId);
		this.strategy = _strategy;
		messageQueue = new LinkedBlockingQueue<IMessage>();
		this.F = _F;
		this.N = _N;
		this.controller = controller;
		
	}

	@Override
	public void run() {
		while (NetworkSimulator.getInstance().isSystemInSimulation()) {
			if (controller.isResetInProgress() && !isLeader()) { // Only non-leader nodes reset here
				//System.out.println("Node :" + nodeId + " is resetting");
				try {
					strategy = new AuthenticatedEchoBroadcastStrategy(N, F);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strategy.setNodeId(nodeId);
				controller.decrementLatch();
				controller.awaitUntillLatch();
			}

			try {
				IMessage message = messageQueue.poll(1000, TimeUnit.MILLISECONDS);
				if (message != null) {

					// broadcast the message to the RabbitMQ
					BroadcastEvent event = new BroadcastEvent(message.getSenderId(), nodeId, message.getType(),NetworkSimulator.getInstance().getNodeName());
					getRabbitMQService().publishMessage(event);

					// Process the message
					if (strategy.executeStrategy(message)) {
						if (isLeader()) { // Leader initiates reset
							controller.initiateReset(N - 1);
							//System.out.println("DELEIVERED>>>>>>>>>>>>>>>>> : Sim Count : " + simulationCount);
							System.out.println(" The Message was delivered by node leader : " + nodeId);
							strategy = new AuthenticatedEchoBroadcastStrategy(N, F);
							strategy.setNodeId(nodeId);
							controller.awaitLatchAndReset();
							setLeader(true); // Start Broadcast again
							simulationCount++;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Exception Occurred here");
				e.printStackTrace();
			}
		}
		System.out.println(" Exiting node run method ");
	}

	/*
	 * Sets a node as the leader and starts broadcasting message
	 */
	public void setLeader(boolean isLeader) {
		System.out.println("Node :" + nodeId + " is elected as leader!!!....");
		this.isLeader = isLeader;
		strategy.leaderBroadcast(new MessageContent("Hello World!!")); // TODO this needs to be better
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
	
	private static RabbitMQService getRabbitMQService() {
        if (rabbitMQService == null) {
            synchronized (Node.class) {
                if (rabbitMQService == null) {
                    try {
                        rabbitMQService = new RabbitMQService("SIMULATION-QUEUE");
                    } catch (Exception e) {
                        LOGGER.info("Issue Initializing the RabbitMQ Service", e);
                        throw new RuntimeException("Failed to initialize RabbitMQ Service", e);
                    }
                }
            }
        }
        return rabbitMQService;
    }

}