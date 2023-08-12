package com.guppy.visualiserweb.data.model;

import com.guppy.visualiserweb.data.model.GraphEvent.MessageType;

/**
 * @author SagarH
 *
 */
public class HighlightEvent {
	
	String leaderNode;
	
	String senderNode;
	
	String receiverNode;
	
	String edgeHighlight;
	
	MessageType eventType;

	public String getLeaderNode() {
		return leaderNode;
	}

	public void setLeaderNode(String leaderNode) {
		this.leaderNode = leaderNode;
	}

	public String getSenderNode() {
		return senderNode;
	}

	public void setSenderNode(String senderNode) {
		this.senderNode = senderNode;
	}

	public String getReceiverNode() {
		return receiverNode;
	}

	public void setReceiverNode(String receiverNode) {
		this.receiverNode = receiverNode;
	}

	public String getEdgeHighlight() {
		return edgeHighlight;
	}

	public void setEdgeHighlight(String edgeHighlight) {
		this.edgeHighlight = edgeHighlight;
	}

	public MessageType getEventType() {
		return eventType;
	}

	public void setEventType(MessageType eventType) {
		this.eventType = eventType;
	}


	

}
