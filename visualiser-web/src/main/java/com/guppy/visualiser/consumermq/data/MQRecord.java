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
package com.guppy.visualiser.consumermq.data;

import java.util.List;

/**
 * Represents a record for an MQ system.
 * 
 * @author SagarH
 */
public class MQRecord {

	/** List of node names associated with the record. */
	private List<NodeName> nodeNames;
	/** Sender node ID. */
	private NodeId senderId;
	/** Receiver node ID. */
	private NodeId receiverId;
	 /** Type of the event. */
	private String eventType;
	/** Time-stamp of the event. */
	private long timeStamp;
	/** Node that leads the event. */
	private String leaderNode;
	/** Highlight of the edge for visualization. */
	private String edgeHighlight;

	/**
	 * Represents a node's name.
	 */
	public static class NodeName {
		private String id;

		/**
         * Retrieves the ID of the node name.
         * @return ID of the node name.
         */
		public String getId() {
			return id;
		}
	}

	/**
	 * Represents a node's ID.
	 */
	public static class NodeId {
		private String id;

		/**
         * Retrieves the ID of the node.
         * @return ID of the node.
         */
		public String getId() {
			return id;
		}
	}

	/**
     * Retrieves the node names.
     * @return List of node names.
     */
	public List<NodeName> getNodeNames() {
		return nodeNames;
	}

	/**
     * Sets the node names.
     * @param nodeNames List of node names.
     */
	public void setNodeNames(List<NodeName> nodeNames) {
		this.nodeNames = nodeNames;
	}

	/**
     * Retrieves the sender's node ID.
     * @return Sender node ID.
     */
	public NodeId getSenderId() {
		return senderId;
	}

	 /**
     * Sets the sender's node ID.
     * @param senderId Sender node ID.
     */
	public void setSenderId(NodeId senderId) {
		this.senderId = senderId;
	}

	/**
     * Retrieves the receiver's node ID.
     * @return Receiver node ID.
     */
	public NodeId getReceiverId() {
		return receiverId;
	}

	/**
     * Sets the receiver's node ID.
     * @param receiverId Receiver node ID.
     */
	public void setReceiverId(NodeId receiverId) {
		this.receiverId = receiverId;
	}

	/**
     * Retrieves the event type.
     * @return Event type.
     */
	public String getEventType() {
		return eventType;
	}

	/**
     * Sets the event type.
     * @param eventType Event type.
     */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
     * Retrieves the time-stamp.
     * @return Event time-stamp.
     */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
     * Sets the time-stamp.
     * @param timeStamp Event time-stamp.
     */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
     * Retrieves the leader node.
     * @return Leader node.
     */
	public String getLeaderNode() {
		return leaderNode;
	}
	
	/**
     * Sets the leader node.
     * @param node Leader node.
     */
	public void setLeaderNode(String node) {
		this.leaderNode = node;
	}

	/**
     * Sets the edge highlight for visualization.
     * @param construtedEdge Edge highlight.
     */
	public void setEdgeHighlight(String construtedEdge) {
		this.edgeHighlight = construtedEdge;
		
	}
	
	/**
     * Retrieves the edge highlight for visualization.
     * @return Edge highlight.
     */
	public String getEdgeHighlight() {
		return edgeHighlight;
	}

}
