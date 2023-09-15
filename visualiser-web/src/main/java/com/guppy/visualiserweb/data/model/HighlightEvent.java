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

import com.guppy.visualiserweb.data.model.GraphEvent.MessageType;

/**
 * Represents an event that highlights specific nodes and edges in the graph.
 * The purpose of this event is to visually indicate important graph
 * interactions or states.
 * 
 * @author SagarH
 */
public class HighlightEvent {

	/**
	 * Node designated as the leader during this highlight event.
	 */
	private String leaderNode;

	/**
	 * Node that sends the message or initiates the action in this event.
	 */
	private String senderNode;

	/**
	 * Node that is intended to receive the message or be the target of the action
	 * in this event.
	 */
	private String receiverNode;

	/**
	 * Specifies the edge to be highlighted in the graph.
	 */
	private String edgeHighlight;

	/**
	 * The type of message/event.
	 */
	private MessageType eventType;

	/**
	 * Retrieves the leader node for this highlight event.
	 * 
	 * @return The node designated as the leader.
	 */
	public String getLeaderNode() {
		return leaderNode;
	}

	/**
	 * Sets the leader node for this highlight event.
	 *
	 * @param leaderNode The node to be designated as the leader.
	 */
	public void setLeaderNode(String leaderNode) {
		this.leaderNode = leaderNode;
	}

	/**
     * Retrieves the sender node of the event.
     * 
     * @return The node that sends the message or initiates the action.
     */
	public String getSenderNode() {
		return senderNode;
	}

	/**
     * Sets the sender node for the event.
     *
     * @param senderNode The node that sends the message or initiates the action.
     */
	public void setSenderNode(String senderNode) {
		this.senderNode = senderNode;
	}

	/**
     * Retrieves the receiver node of the event.
     * 
     * @return The node that is intended to receive the message or be the target of the action.
     */
	public String getReceiverNode() {
		return receiverNode;
	}

	/**
     * Sets the receiver node for the event.
     *
     * @param receiverNode The node that is intended to receive the message or be the target of the action.
     */
	public void setReceiverNode(String receiverNode) {
		this.receiverNode = receiverNode;
	}

	/**
     * Gets the edge to be highlighted.
     * 
     * @return The edge to be highlighted.
     */
	public String getEdgeHighlight() {
		return edgeHighlight;
	}

	/**
     * Sets which edge should be highlighted in the graph.
     * 
     * @param edgeHighlight The edge to be highlighted.
     */
	public void setEdgeHighlight(String edgeHighlight) {
		this.edgeHighlight = edgeHighlight;
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

}
