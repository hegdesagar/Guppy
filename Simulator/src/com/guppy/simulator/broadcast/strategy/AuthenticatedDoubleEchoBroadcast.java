/*
====================================================
Copyright (c) 2023 SagarH
All Rights Reserved.
Permission to use, copy, modify, and distribute this software and its
documentation for any purpose, without fee, and without a written agreement is hereby granted, 
provide that the above copyright notice and this paragraph and the following two paragraphs appear in all copies.

IN NO EVENT SHALL YOUR NAME BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF YOU HAVE BEEN
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

SagarH SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND YOUR NAME HAS NO
OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
====================================================
*/
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

/**
 * Represents the AuthenticatedDoubleEchoBroadcast strategy for broadcasting messages.
 * This strategy uses a double echo mechanism to ensure message authenticity and 
 * resilience against faulty nodes.
 *
 * @see AbstractBroadcastStrategy
 * @see IBroadcastStrategy
 */
@BroadCastStrategy("AuthenticatedDoubleEchoBroadcast")
public class AuthenticatedDoubleEchoBroadcast extends AbstractBroadcastStrategy implements IBroadcastStrategy {

	 /** Logger for the class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedDoubleEchoBroadcast.class);
	/** Indicates whether the message has been delivered. */
	private boolean delivered;
	/** Set to store messages that have been echoed. */
    private Set<IMessage> echoedMessages = new HashSet<>();
    /** Set to store messages for which ready has been sent. */
    private Set<IMessage> readySentMessages = new HashSet<>();
    /** Set to store received ready messages. */
    private Set<IMessage> readyReceivedMessages = new HashSet<>();

    /**
     * Constructor initializing the AuthenticatedDoubleEchoBroadcast strategy.
     *
     * @param _N      number of nodes
     * @param _f      number of faulty nodes
     * @param nodeId  the node ID
     * @throws Exception 
     */
	public AuthenticatedDoubleEchoBroadcast(int _N, int _f, NodeId nodeId) throws Exception {
		super(_N, _f, nodeId);
		delivered = false;
	}

	/** 
     * @inheritDoc 
     */
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

	/** 
     * Handles the sending of messages.
     */
    private void handleSend(IMessage message, long latency) {
        if (!hasAlreadyEchoed(message)) {
            echoedMessages.add(message);
            IMessage echoMessage = new Message(nodeId, message.getContent(), MessageType.ECHO, message.getIteration());
            broadcastMessage(echoMessage, latency);
        }
    }

    /** 
     * Handles the echoing of messages.
     */
    private void handleEcho(IMessage message, long latency) {
        if (!hasAlreadySentReady(message)) {
            readySentMessages.add(message);
            IMessage readyMessage = new Message(nodeId, message.getContent(), MessageType.READY, message.getIteration());
            broadcastMessage(readyMessage, latency);
        }
    }

    /** 
     * Handles ready messages.
     */
    private void handleReady(IMessage message) {
        readyReceivedMessages.add(message);
    }

    /** 
     * Checks if a message has already been echoed.
     */
    private boolean hasAlreadyEchoed(IMessage message) {
        return echoedMessages.contains(message);
    }

    /** 
     * Checks if a ready message has already been sent for a particular message.
     */
    private boolean hasAlreadySentReady(IMessage message) {
        return readySentMessages.contains(message);
    }

    /** 
     * Counts the number of ready messages for a given message.
     */
    private int countReadyMessages(IMessage message) {
        int count = 0;
        for (IMessage readyMessage : readyReceivedMessages) {
            if (readyMessage.getContent().equals(message.getContent())) {
                count++;
            }
        }
        return count;
    }

    /** 
     * Checks if a message can be delivered.
     */
    private boolean checkForDelivery(IMessage message) {
        if (countReadyMessages(message) > ((N - F) / 2) && !delivered) {
            delivered = true;
            LOGGER.info("Node : {}  Message delivered ", nodeId);
            return true;
        }
        return false;
    }

    /** 
     * @inheritDoc 
     */
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

	/** 
     * Broadcasts a message to all nodes.
     */
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
	
	/** 
     * @inheritDoc 
     */
	@Override
	public boolean isDelivered() {
		return delivered;
	}

	/** 
     * Checks if a node is a leader.
     */
	private boolean isNodeLeader(NodeId id) {
		for (INode node : NetworkSimulator.getInstance().getNodes()) {
			if (node.getNodeId().equals(id)) {
				return node.isLeader();
			}
		}
		return false;
	}

	/** 
     * @inheritDoc 
     */
	@Override
	public void reset() {

		//sentEcho = false;
		delivered = false;
		echoedMessages.clear();
		readySentMessages.clear();
		readyReceivedMessages.clear();
	}

	/** 
     * Determines whether to drop a message.
     */
	private boolean dropMessage() {
		Random rand = new Random();
		return rand.nextInt(100) < 50; // 50% chance to behave as a Byzantine node and not send a message
	}

	/** 
     * Floods messages.
     */
	public void floodMessages(IMessage message, long latency) throws Exception {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			for (int i = 0; i < 5; i++) {
				echoedMessages.add(message);
				broadcastMessage(message, latency);
			}
		}
	}

	/** 
     * Alters a message.
     */
	public IMessage alterMessage(IMessage message) {
		Random rand = new Random();
		if (rand.nextInt(100) < 50) { // 50% chance to behave as a Byzantine node
			MessageContent content = new MessageContent("Tampered " + message.getContent());
			return new Message(nodeId, content, MessageType.ECHO, generateIteration());
		}
		return message;
	}

	/** 
     * Publishes an undelivered message.
     */
	@Override
	public void publishNotDelivered(IMessage message) {
		BroadcastEvent event = new BroadcastEvent(nodeId, nodeId, MessageType.NOTDELIVERED,
				NetworkSimulator.getInstance().getNodeName(), 0);
		producer.produce(event);

	}

}
