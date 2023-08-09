package com.guppy.simulator.broadcast.strategy;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.guppy.simulator.broadcast.events.BroadcastEvent;
import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.Message;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.distributed.node.INode;

/**
 * 
 * @author SagarHegde
 *
 */
public final class AuthenticatedEchoBroadcastStrategy implements IBroadcastStrategy {

	private boolean sentEcho;
	private boolean delivered;
	private int N; // Number of nodes
	private int f; // Maximum number of faulty nodes
	private NodeId nodeId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedEchoBroadcastStrategy.class);

	private List<IMessage> echoMessages = new LinkedList<IMessage>();


	public AuthenticatedEchoBroadcastStrategy(int _N, int _f) throws Exception {
		this.N = _N;
		this.f = _f;
		sentEcho = false;
		delivered = false;
	}

	@Override
	public boolean executeStrategy(IMessage message) throws Exception {
		if (MessageType.SEND.equals(message.getType())) {
			if (message.getSenderId().equals(nodeId) && !sentEcho) {
				// If this node is the sender of the SEND message
				sentEcho = true;
				// Add the sender's own echo message in the echoMessages map
				Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);
				echoMessages.add(echoMessage);
				// Broadcast the original SEND message
				broadcastMessage(message);
			} else {
				// If this node is not the sender of the SEND message
				// Create a new ECHO message and broadcast it
				Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);
				echoMessages.add(echoMessage);
				broadcastMessage(echoMessage);
			}

		} else if (MessageType.ECHO.equals(message.getType()) && !isAlreadyEchoed(message)) {
			echoMessages.add(message);
		}
		int echoCount = getEchoCount(message);
		if (echoCount > (N - f) / 2 && !delivered && isNodeLeader(nodeId)) {
			this.delivered  = true;
			LOGGER.info("Node : {}  Message delivered ", nodeId);
			return true;
		}
		return false;
	}

	@Override
	public boolean leaderBroadcast(MessageContent content) {
		// Create a new SEND message with the given content
		Message sendMessage = new Message(nodeId, content, MessageType.SEND);
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
		for(IMessage msg: echoMessages) {
			if(msg.getSenderId().equals(senderId)) {
				return true;
			}
		}
		return false;
	}
	
	private void broadcastMessage(IMessage message) {
		// Create a new ECHO message with the same content as the original message
		Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);

		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			try {
				node.publishMessage(echoMessage);
			} catch (InterruptedException e) {
				LOGGER.info("Node : {} Thread interuppted while broadcasting.", nodeId);
				Thread.currentThread().interrupt();
			}
		}

	}
	
	@Override
	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	private boolean isNodeLeader(NodeId id) {
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			if(node.getNodeId().equals(id)) {
				return node.isLeader();
			}
		}
		return false;
	}

}
