package com.guppy.simulator.broadcast.events;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.EventType;
import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.NodeId;

public class BroadcastEvent {

	private NodeId senderId;
	private NodeId receiverId;
	private EventType eventType;
	private long timeStamp;

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

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BroadcastEvent(NodeId senderId, NodeId receiverId, EventType eventType) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.eventType = eventType;
		this.timeStamp = System.currentTimeMillis();
	}

}
