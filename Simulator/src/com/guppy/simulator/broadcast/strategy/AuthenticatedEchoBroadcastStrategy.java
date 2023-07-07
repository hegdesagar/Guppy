package com.guppy.simulator.broadcast.strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.broadcast.message.Message;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.core.NetworkSimulator;
import com.guppy.simulator.distributed.node.INode;

public class AuthenticatedEchoBroadcastStrategy implements IBroadcastStrategy {

	private boolean sentEcho;
	private boolean delivered;
	private int N; // Number of nodes
	private int f; // Maximum number of faulty nodes
	private NodeId nodeId;

	// TODO is concurrentHashMap really required here?
	private List<IMessage> echoMessages = new LinkedList<IMessage>();

	public AuthenticatedEchoBroadcastStrategy(int _N, int _f) {
		this.N = _N;
		this.f = _f;
		sentEcho = false;
		delivered = false;

	}

	@Override
	public void executeStrategy(IMessage message) {
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
		if (echoCount > (N - f) / 2 && !delivered) {
			delivered = true;
			deliver(message);
		}
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
	
	@Override
	public void broadcastMessage(IMessage message) {
		// Create a new ECHO message with the same content as the original message
		Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);

		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			BlockingQueue<IMessage> queue = node.getMessageQueue();
			try {
				queue.put(echoMessage);
			} catch (InterruptedException e) {
				// TODO: handle exception
				Thread.currentThread().interrupt();
			}
		}

	}
	
	public void deliver(IMessage message) {
		System.out.println("Node " + nodeId + " delivered message: " + message.getContent());
	}
	
	public int getEchoCount(IMessage message) {
	    int count = 0;
	    for (IMessage echoMessage : echoMessages) {
	        if (echoMessage.getContent().equals(message.getContent())) {
	            count++;
	        }
	    }
	    return count;
	}
	
	@Override
	public void leaderBroadcast(MessageContent content) {
		// Create a new SEND message with the given content
		Message sendMessage = new Message(nodeId, content, MessageType.SEND);
		// Broadcast the SEND message to all other nodes
		System.out.println("Number of nodes are :"+NetworkSimulator.getInstance().getNodes());
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			BlockingQueue<IMessage> queue = node.getMessageQueue();
			try {
				queue.put(sendMessage);
			} catch (InterruptedException e) {
				// TODO: handle exception
				Thread.currentThread().interrupt();
			}
		}

	}
	
	@Override
	public NodeId getNodeId() {
		return nodeId;
	}
	
	@Override
	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

}
