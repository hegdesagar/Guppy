package com.guppy.simulator.broadcast.message;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;

public interface IMessage {

	public MessageType getType();
	
	public void setType(MessageType type);

	public NodeId getSenderId();
	
	public MessageContent getContent();
	
	public AtomicLong getIteration();
	
	public void setIteration(AtomicLong iteration);
	
	public LocalDateTime getTimeStamp();

	public void setTimeStamp(LocalDateTime timeStamp);

}
