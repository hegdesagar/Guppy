package com.guppy.simulator.broadcast.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

@BroadCastStrategy("CPABroadcast")
public class CPABroadcastStrategy extends AbstractBroadcastStrategy implements IBroadcastStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(CPABroadcastStrategy.class);

	private final Set<MessageContent> deliveredMessages = new HashSet<>();

	private final HashMap<NodeId, IMessage> receivedMessages = new HashMap<>();

	private boolean delivered;

	public CPABroadcastStrategy(int _N, int _f, NodeId nodeId) {
		super(_N, _f, nodeId);
	}

	@Override
	public boolean executeStrategy(IMessage message,long latency) throws Exception {

		// Drop the message
		if (maybeDontSendMessage && dropMessage()) {
			LOGGER.warn("Node : {}  Message Dropped ", nodeId);
			return false;
		}

		switch (message.getType()) {
		case INIT:
			if (!receivedMessages.containsKey(message.getSenderId())) {
				if (maybeAlterMessageContent) { // Alter the message
					LOGGER.warn("Node : {}  Message Altered ", nodeId);
					message = alterMessage(message);
				}
				receivedMessages.put(message.getSenderId(), message);
				broadcastMessage(new Message(nodeId, message.getContent(), MessageType.ECHO, generateIteration()),latency);
			}
			break;
		case ECHO:
			receivedMessages.put(message.getSenderId(), message);
			int echoCount = countMessagesOfTypeForValue(MessageType.ECHO, message.getContent());
			if (echoCount > N - F && !hasSentReadyForValue(message.getContent())) {
				IMessage echoMessage = new Message(nodeId, message.getContent(), Message.MessageType.READY, generateIteration());
				if (maybeAlterMessageContent) { // Alter the message
					LOGGER.warn("Node : {}  Message Altered ", nodeId);
					echoMessage = alterMessage(echoMessage);
				}if (maybeSendRedundantMessages) {
					LOGGER.warn("Node : {}  Flooding Messaged ", nodeId);
					floodMessages(echoMessage,latency);
				} else {
					broadcastMessage(echoMessage,latency);
				}
			}
			break;
		case READY:
			receivedMessages.put(message.getSenderId(), message);
			int readyCount = countMessagesOfTypeForValue(MessageType.READY, message.getContent());
			if (readyCount > 2 * F && !hasSentEchoForValue(message.getContent())) {
				broadcastMessage(new Message(nodeId, message.getContent(), MessageType.ECHO, generateIteration()),latency);
			}
			if (readyCount > F && !hasDeliveredMessageWithValue(message.getContent())) {
				// Deliver the message
				deliver(message);
				delivered = true;
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}

	private void broadcastMessage(IMessage message,long latency) {
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			try {
				node.publishMessage(message);
				// Publish the Delivered event to the queue
				BroadcastEvent event = new BroadcastEvent(message.getSenderId(), node.getNodeId(), message.getType(),
						NetworkSimulator.getInstance().getNodeName(),latency);
				producer.produce(event);
			} catch (InterruptedException e) {
				LOGGER.info("Node : {} Thread interuppted while broadcasting.", nodeId);
				Thread.currentThread().interrupt();
			}
		}

	}

	private int countMessagesOfTypeForValue(MessageType type, MessageContent value) {
		System.out.println("Total messages: " + receivedMessages.size());
		System.out.println("Checking for type: " + type + " and value: " + value);
		receivedMessages.values().forEach(msg -> {
		    System.out.println("Message type: " + msg.getType() + ", content: " + msg.getContent());
		});
		
		return (int) receivedMessages.values().stream()
				.filter(msg -> msg.getType().equals(type) && msg.getContent().equals(value)).count();
	}

	private boolean hasSentEchoForValue(MessageContent value) {
		return receivedMessages.values().stream().anyMatch(msg -> msg.getSenderId().getId().equals(nodeId.getId())
				&& msg.getType().equals(MessageType.ECHO) && msg.getContent().equals(value));
	}

	private boolean hasSentReadyForValue(MessageContent value) {
		return receivedMessages.values().stream().anyMatch(msg -> msg.getSenderId().getId().equals(nodeId.getId())
				&& msg.getType().equals(MessageType.READY) && msg.getContent().equals(value));
	}

	private boolean hasDeliveredMessageWithValue(MessageContent value) {
		return deliveredMessages.contains(value);
	}

	private void deliver(IMessage message) {
		// Add to the delivered messages set
		deliveredMessages.add(message.getContent());
		LOGGER.info("Message delivered: {}", message.getContent());
	}

	@Override
	public boolean leaderBroadcast(MessageContent content) {
		// Create a new INIT message with the given content
		Message initMessage = new Message(nodeId, content, MessageType.INIT, generateIteration());

		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			BroadcastEvent event = new BroadcastEvent(initMessage.getSenderId(), node.getNodeId(), MessageType.INIT,
					NetworkSimulator.getInstance().getNodeName(),0);
			producer.produce(event);
		}
		// Broadcast the INIT message to all other nodes
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			try {
				node.publishMessage(initMessage);
			} catch (InterruptedException e) {
				LOGGER.info("Node : {} Thread interuppted while leader broadcast.", nodeId);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isDelivered() {
		return delivered;
	}

	@Override
	public void reset() {
		delivered = false;
		deliveredMessages.clear();
		receivedMessages.clear();
	}

	@Override
	public void publishNotDelivered(IMessage message) {
		BroadcastEvent event = new BroadcastEvent(nodeId, nodeId, MessageType.NOTDELIVERED,
				NetworkSimulator.getInstance().getNodeName(),0);
		producer.produce(event);

	}
	
	private boolean dropMessage() {
		Random rand = new Random();
		return rand.nextInt(100) < 50; // 50% chance to behave as a Byzantine node and not send a message
	}

	private void floodMessages(IMessage message,long latency) throws Exception {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			for (int i = 0; i < 5; i++) {
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

}
