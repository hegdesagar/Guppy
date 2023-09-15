package com.guppy.simulator.broadcast.strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.Message;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.broadcast.strategy.annotation.BroadCastStrategy;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.distributed.node.INode;

/**
 * Implementation of an authenticated echo broadcasting strategy. 
 * It ensures reliable broadcasting of messages even in the presence of Byzantine failures, 
 * which can manifest in the form of nodes altering messages, not sending messages, or flooding the network with redundant messages.
 * <p>
 * The strategy works as follows:
 * <ul>
 *   <li>A leader node sends a message.</li>
 *   <li>Receiving nodes, upon getting the message, send an echo message to all other nodes.</li>
 *   <li>If a node receives more than (N - F) / 2 echo messages for the same original message, 
 *       where N is the total number of nodes and F is the number of faulty nodes, 
 *       it considers the message delivered.</li>
 * </ul>
 * </p>
 * <p>
 * Nodes can also simulate Byzantine behaviors, such as message alteration, message drop, and message flooding.
 * </p>
 * <p>
 * This strategy extends the {@code AbstractBroadcastStrategy} and implements the {@code IBroadcastStrategy} interface.
 * </p>
 * 
 * @author SagarHegde
 * @see IBroadcastStrategy
 */
@BroadCastStrategy("AuthenticatedEchoBroadcast")
public final class AuthenticatedEchoBroadcastStrategy extends AbstractBroadcastStrategy implements IBroadcastStrategy {

	private boolean sentEcho;
	private boolean delivered;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedEchoBroadcastStrategy.class);

	private List<IMessage> echoMessages = new LinkedList<IMessage>();

	public AuthenticatedEchoBroadcastStrategy(int _N, int _f, NodeId nodeId) throws Exception {
		super(_N, _f, nodeId);
		sentEcho = false;
		delivered = false;
	}

	@Override
	public boolean executeStrategy(IMessage message,long latency) throws Exception {

		// Drop the message
		if (maybeDontSendMessage && dropMessage()) {
			LOGGER.warn("Node : {}  Message Dropped ", nodeId);
			return false;
		}

		// Process the message
		if (MessageType.SEND.equals(message.getType())) {
			if (message.getSenderId().equals(nodeId) && !sentEcho) {
				// If this node is the sender of the SEND message
				if (maybeAlterMessageContent) { // Alter the message
					LOGGER.warn("Node : {}  Message Altered ", nodeId);
					message = alterMessage(message);
				}
				sentEcho = true;
				// Broadcast the original SEND message
				broadcastMessage(message,latency);
			} else {
				// If this node is not the sender of the SEND message
				// Create a new ECHO message and broadcast it
				IMessage echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO,
						message.getIteration());
				if (maybeAlterMessageContent) { // Alter the message
					LOGGER.warn("Node : {}  Message Altered ", nodeId);
					echoMessage = alterMessage(echoMessage);
				}
				if (maybeSendRedundantMessages) {
					LOGGER.warn("Node : {}  Flooding Messaged ", nodeId);
					floodMessages(echoMessage,latency);
				} else {
					echoMessages.add(echoMessage);
					broadcastMessage(echoMessage,latency);
				}
			}

		} else if (MessageType.ECHO.equals(message.getType()) && !isAlreadyEchoed(message)) {
			echoMessages.add(message);
		}
		int echoCount = getEchoCount(message);
		if (echoCount > ((N - F) / 2) + 1 && !delivered && isNodeLeader(nodeId)) {
			// if (echoCount >5 && !delivered && isNodeLeader(nodeId)) { //test logic
			this.delivered = true;
			LOGGER.info("Node : {}  Message delivered ", nodeId);
			return true;
		}
		return false;
	}

	@Override
	public boolean leaderBroadcast(MessageContent content) {
		// Create a new SEND message with the given content
		Message sendMessage = new Message(nodeId, content, MessageType.SEND, generateIteration());

		// Publish these events to the MQ
		// Publish the Delivered event to the queue
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			BroadcastEvent event = new BroadcastEvent(sendMessage.getSenderId(), node.getNodeId(), MessageType.SEND,
					NetworkSimulator.getInstance().getNodeName(),0);
			producer.produce(event);
		}
		// Broadcast the SEND message to all other nodes
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			try {
				node.publishMessage(sendMessage);
			} catch (InterruptedException e) {
				LOGGER.info("Node : {} Thread interuppted while leader broadcast.", nodeId);
				return false;
			}
		}
		return true;
	}

	protected void broadcastMessage(IMessage message,long latency) {
		// Create a new ECHO message with the same content as the original message
		Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO, generateIteration());

		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			try {
				node.publishMessage(echoMessage);
				// Publish the Delivered event to the queue
				if (!(isNodeLeader(nodeId) && message.getType().equals(MessageType.SEND))) {
					BroadcastEvent event = new BroadcastEvent(echoMessage.getSenderId(), node.getNodeId(),
							MessageType.ECHO, NetworkSimulator.getInstance().getNodeName(),latency);
					producer.produce(event);
				}
			} catch (InterruptedException e) {
				LOGGER.info("Node : {} Thread interuppted while broadcasting.", nodeId);
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public boolean isDelivered() {
		return delivered;
	}

	private int getEchoCount(IMessage message) {
		int count = 0;
		for (IMessage echoMessage : echoMessages) {
			if (echoMessage.getContent().equals(message.getContent())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @param message
	 */
	private boolean isAlreadyEchoed(IMessage message) {
		NodeId senderId = message.getSenderId();
		for (IMessage msg : echoMessages) {
			if (msg.getSenderId().equals(senderId)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * @Override public void setNodeId(NodeId nodeId) { this.nodeId = nodeId; }
	 */

	private boolean isNodeLeader(NodeId id) {
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			if (node.getNodeId().equals(id)) {
				return node.isLeader();
			}
		}
		return false;
	}

	@Override
	public void reset() {

		sentEcho = false;
		delivered = false;
		echoMessages.clear();
	}

	private boolean dropMessage() {
		Random rand = new Random();
		return rand.nextInt(100) < 50; // 50% chance to behave as a Byzantine node and not send a message
	}

	private void floodMessages(IMessage message,long latency) throws Exception {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			for (int i = 0; i < 5; i++) {
				echoMessages.add(message);
				broadcastMessage(message,latency);
			}
		}
	}

	private IMessage alterMessage(IMessage message) {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			MessageContent content = new MessageContent("Tampered " + message.getContent());
			return new Message(nodeId, content, MessageType.ECHO, generateIteration());
		}
		return message;
	}

	@Override
	public void publishNotDelivered(IMessage message) {
		BroadcastEvent event = new BroadcastEvent(nodeId, nodeId, MessageType.NOTDELIVERED,
				NetworkSimulator.getInstance().getNodeName(),0);
		producer.produce(event);

	}

}
