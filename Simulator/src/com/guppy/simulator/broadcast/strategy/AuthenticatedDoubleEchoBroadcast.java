package com.guppy.simulator.broadcast.strategy;

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

@BroadCastStrategy("AuthenticatedDoubleEchoBroadcast")
public class AuthenticatedDoubleEchoBroadcast extends AbstractBroadcastStrategy implements IBroadcastStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedDoubleEchoBroadcast.class);
	//private boolean sentEcho;
	private boolean delivered;
    private Set<IMessage> echoedMessages = new HashSet<>();
    private Set<IMessage> readySentMessages = new HashSet<>();
    private Set<IMessage> readyReceivedMessages = new HashSet<>();

	public AuthenticatedDoubleEchoBroadcast(int _N, int _f, NodeId nodeId) throws Exception {
		super(_N, _f, nodeId);
		//sentEcho = false;
		delivered = false;
	}

	@Override
    public boolean executeStrategy(IMessage message, long latency) throws Exception {
        if (maybeDontSendMessage && dropMessage()) {
            LOGGER.warn("Node : {}  Message Dropped ", nodeId);
            return false;
        }

        switch (message.getType()) {
            case SEND:
                handleSend(message, latency);
                break;
            case ECHO:
                handleEcho(message, latency);
                break;
            case READY:
                handleReady(message);
                break;
		default:
			break;
        }
        
        return checkForDelivery(message);
    }

    private void handleSend(IMessage message, long latency) {
        if (!hasAlreadyEchoed(message)) {
            echoedMessages.add(message);
            IMessage echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO, message.getIteration());
            broadcastMessage(echoMessage, latency);
        }
    }

    private void handleEcho(IMessage message, long latency) {
        if (!hasAlreadySentReady(message)) {
            readySentMessages.add(message);
            IMessage readyMessage = new Message(nodeId, message.getContent(), MessageType.READY, message.getIteration());
            broadcastMessage(readyMessage, latency);
        }
    }

    private void handleReady(IMessage message) {
        readyReceivedMessages.add(message);
    }

    private boolean hasAlreadyEchoed(IMessage message) {
        return echoedMessages.contains(message);
    }

    private boolean hasAlreadySentReady(IMessage message) {
        return readySentMessages.contains(message);
    }

    private int countReadyMessages(IMessage message) {
        int count = 0;
        for (IMessage readyMessage : readyReceivedMessages) {
            if (readyMessage.getContent().equals(message.getContent())) {
                count++;
            }
        }
        return count;
    }

    private boolean checkForDelivery(IMessage message) {
        if (countReadyMessages(message) > ((N - F) / 2) && !delivered) {
            delivered = true;
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
					NetworkSimulator.getInstance().getNodeName(), 0);
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

	protected void broadcastMessage(IMessage message, long latency) {
	    for (INode node : NetworkSimulator.getInstance().getNodes()) {
	        try {
	            node.publishMessage(message);
	            
	            if (!(isNodeLeader(nodeId) && message.getType().equals(MessageType.SEND))) {
	                BroadcastEvent event = new BroadcastEvent(message.getSenderId(), node.getNodeId(),
	                        message.getType(), NetworkSimulator.getInstance().getNodeName(), latency);
	                producer.produce(event);
	            }
	        } catch (InterruptedException e) {
	            LOGGER.info("Node : {} Thread interrupted while broadcasting.", nodeId);
	            Thread.currentThread().interrupt();
	        }
	    }
	}

	@Override
	public boolean isDelivered() {
		return delivered;
	}


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

		//sentEcho = false;
		delivered = false;
		echoedMessages.clear();
		readySentMessages.clear();
		readyReceivedMessages.clear();
	}

	private boolean dropMessage() {
		Random rand = new Random();
		return rand.nextInt(100) < 50; // 50% chance to behave as a Byzantine node and not send a message
	}

	private void floodMessages(IMessage message, long latency) throws Exception {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			for (int i = 0; i < 5; i++) {
				echoedMessages.add(message);
				broadcastMessage(message, latency);
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
				NetworkSimulator.getInstance().getNodeName(), 0);
		producer.produce(event);

	}

}
