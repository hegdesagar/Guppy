package com.guppy.visualiserweb.data.model;

import java.util.ArrayList;

public class GraphEvent {
	public enum MessageType {
		SEND, ECHO, DELIVERED
	};

	private ArrayList<NodeData> nodeNames;
	private NodeData senderId;
	private NodeData receiverId;
	private MessageType eventType;
	private long timeStamp;


	public NodeData getSenderId() {
		return senderId;
	}

	public void setSenderId(NodeData senderId) {
		this.senderId = senderId;
	}

	public NodeData getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(NodeData receiverId) {
		this.receiverId = receiverId;
	}

	public MessageType getEventType() {
		return eventType;
	}

	public void setEventType(MessageType eventType) {
		this.eventType = eventType;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ArrayList<NodeData> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<NodeData> nodeNames) {
		this.nodeNames = nodeNames;
	}
}
