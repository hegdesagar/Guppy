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
package com.guppy.simulator.broadcast.events;

import java.util.ArrayList;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * Represents an event triggered during broadcasting in a network.
 * 
 * @author HegdeSagar
 */
public class BroadcastEvent {
	
	private ArrayList<NodeId> nodeNames;
	private NodeId senderId;
	private NodeId receiverId;
	private MessageType eventType;
	private long timeStamp;
	
	/**
     * Initializes a new instance of the {@code BroadcastEvent} class.
     * 
     * @param senderId    The ID of the node that sent the message.
     * @param receiverId  The ID of the node that received the message.
     * @param eventType   The type of message event.
     * @param nodeNames   A list of node names involved in this event.
     * @param latency     The latency associated with the event.
     */
	public BroadcastEvent(NodeId senderId, NodeId receiverId, MessageType eventType, ArrayList<NodeId> nodeNames,long latency) {
		this.nodeNames = nodeNames;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.eventType = eventType;
		this.timeStamp = latency;
	}

	/**
     * Gets the list of node names.
     * 
     * @return A list of node names involved in this event.
     */
	public ArrayList<NodeId> getNodeNames() {
		return nodeNames;
	}

	/**
     * Sets the list of node names.
     * 
     * @param nodeNames  A list of node names.
     */
	public void setNodeNames(ArrayList<NodeId> nodeNames) {
		this.nodeNames = nodeNames;
	}
	
	/**
     * Gets the sender's ID.
     * 
     * @return The ID of the node that sent the message.
     */
	public NodeId getSenderId() {
		return senderId;
	}

	/**
     * Sets the sender's ID.
     * 
     * @param senderId  The ID of the node that sent the message.
     */
	public void setSenderId(NodeId senderId) {
		this.senderId = senderId;
	}

	/**
     * Gets the receiver's ID.
     * 
     * @return The ID of the node that received the message.
     */
	public NodeId getReceiverId() {
		return receiverId;
	}

	/**
     * Sets the receiver's ID.
     * 
     * @param receiverId  The ID of the node that received the message.
     */
	public void setReceiverId(NodeId receiverId) {
		this.receiverId = receiverId;
	}

	/**
     * Gets the event type.
     * 
     * @return The type of message event.
     */
	public MessageType getEventType() {
		return eventType;
	}

	/**
     * Sets the event type.
     * 
     * @param eventType  The type of message event.
     */
	public void setEventType(MessageType eventType) {
		this.eventType = eventType;
	}

	/**
     * Gets the event's timestamp.
     * 
     * @return The latency associated with the event.
     */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
     * Sets the event's timestamp.
     * 
     * @param timeStamp  The latency to be associated with the event.
     */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}


}
