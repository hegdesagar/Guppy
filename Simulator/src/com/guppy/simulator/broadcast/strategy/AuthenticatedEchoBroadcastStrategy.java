package com.guppy.simulator.broadcast.strategy;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.guppy.simulator.broadcast.message.IMessage;

public class AuthenticatedEchoBroadcastStrategy implements IBroadcastStrategy {

	private boolean sentEcho;
	private boolean delivered;
	private int N; // Number of nodes
	private int f; // Maximum number of faulty nodes
	private String nodeId;

	// TODO is concurrentHashMap really required here?
	private Map<String, IMessage> echoMessages = new HasMap<String, IMessage>();

	public AuthenticatedEchoBroadcastStrategy(String _nodeId, int _N, int _f) {
		this.nodeId = _nodeId;
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
				echoMessages.put(nodeId, echoMessage);
				// Broadcast the original SEND message
				broadcastMessage(message);
			} else {
				// If this node is not the sender of the SEND message
				// Create a new ECHO message and broadcast it
				Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);
				echoMessages.put(nodeId, echoMessage);
				broadcastMessage(echoMessage);
			}
		} else if (MessageType.ECHO.equals(message.getType()) && !echoMessages.containsKey(message.getSenderId())) {
			echoMessages.put(message.getSenderId(), message);
		}
		int echoCount = getEchoCount(message);
		if (echoCount > (N - f) / 2 && !delivered) {
			delivered = true;
			deliver(message);
		}
	}
	
	@Override
	public void broadcastMessage(IMessage message) {
		// Create a new ECHO message with the same content as the original message
		Message echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO);

		for (INode node : Simulator.getInstance().getNodes()) {
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
	    for (IMessage echoMessage : echoMessages.values()) {
	        if (echoMessage.getContent().equals(message.getContent())) {
	            count++;
	        }
	    }
	    return count;
	}
	
	@Override
	public void leaderBroadcast(String content) {
		// Create a new SEND message with the given content
		Message sendMessage = new Message(nodeId, content, MessageType.SEND);
		// Broadcast the SEND message to all other nodes
		System.out.println("Number of nodes are :"+Simulator.getInstance().getNoOfNodes());
		for (INode node : Simulator.getInstance().getNodes()) {
			BlockingQueue<IMessage> queue = node.getMessageQueue();
			try {
				queue.put(sendMessage);
			} catch (InterruptedException e) {
				// TODO: handle exception
				Thread.currentThread().interrupt();
			}
		}

	}

}
