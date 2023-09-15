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
package com.guppy.visualiserweb.data.model;

import java.util.ArrayList;

/**
 * Represents an event occurring in the graph, detailing interactions between nodes.
 * 
 * @author HegdeSagar
 */
public class GraphEvent {
	/**
     * Enumerates the types of messages that can be part of a graph event.
     */
	public enum MessageType {
		SEND,      /** Represents a message being sent from one node to another. */
		ECHO,      /** Represents an echo message. */
		DELIVERED  /** Represents a message that has been successfully delivered. */
	};

    /**
     * List of node names/data that are part of this event.
     */
	private ArrayList<NodeData> nodeNames;

    /**
     * The node that sent the message in this event.
     */
	private NodeData senderId;

    /**
     * The node that is intended to receive the message in this event.
     */
	private NodeData receiverId;

    /**
     * The type of message/event.
     */
	private MessageType eventType;

    /**
     * The timestamp of when this event occurred.
     */
	private long timeStamp;

    /**
     * Gets the sender node of the event.
     *
     * @return The node data of the sender.
     */
	public NodeData getSenderId() {
		return senderId;
	}

	/**
     * Sets the sender node for the event.
     *
     * @param senderId The node data of the sender.
     */
	public void setSenderId(NodeData senderId) {
		this.senderId = senderId;
	}

	/**
     * Gets the receiver node of the event.
     *
     * @return The node data of the receiver.
     */
	public NodeData getReceiverId() {
		return receiverId;
	}

	/**
     * Sets the receiver node for the event.
     *
     * @param receiverId The node data of the receiver.
     */
	public void setReceiverId(NodeData receiverId) {
		this.receiverId = receiverId;
	}

	/**
     * Gets the type of the event/message.
     *
     * @return The event type.
     */
	public MessageType getEventType() {
		return eventType;
	}

	/**
     * Sets the type of the event/message.
     *
     * @param eventType The event type.
     */
	public void setEventType(MessageType eventType) {
		this.eventType = eventType;
	}

	/**
     * Gets the timestamp of the event.
     *
     * @return The timestamp of the event.
     */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
     * Sets the timestamp of the event.
     *
     * @param timeStamp The timestamp to be set.
     */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	 /**
     * Gets the list of node names/data associated with this event.
     *
     * @return The list of node names/data.
     */
	public ArrayList<NodeData> getNodeNames() {
		return nodeNames;
	}

	/**
     * Sets the list of node names/data for this event.
     *
     * @param nodeNames The list of node names/data to be set.
     */
	public void setNodeNames(ArrayList<NodeData> nodeNames) {
		this.nodeNames = nodeNames;
	}
}
