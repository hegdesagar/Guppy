package com.guppy.simulator.broadcast.events;

import java.util.ArrayList;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.NodeId;

public class BroadcastEvent {

	private ArrayList<NodeId> nodeNames;
	private NodeId senderId;
	private NodeId receiverId;
	private MessageType eventType;
	private long timeStamp;

	
	public ArrayList<NodeId> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<NodeId> nodeNames) {
		this.nodeNames = nodeNames;
	}
	
	public NodeId getSenderId() {
		return senderId;
	}

	public void setSenderId(NodeId senderId) {
		this.senderId = senderId;
	}

	public NodeId getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(NodeId receiverId) {
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

	public BroadcastEvent(NodeId senderId, NodeId receiverId, MessageType eventType, ArrayList<NodeId> nodeNames) {
		super();
		this.nodeNames = nodeNames;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.eventType = eventType;
		this.timeStamp = System.currentTimeMillis();
	}

}
