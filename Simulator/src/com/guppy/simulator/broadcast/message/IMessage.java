package com.guppy.simulator.broadcast.message;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.NodeId;

public interface IMessage {

	public MessageType getType();
	
	public void setType(MessageType type);

	public NodeId getSenderId();
	
	public MessageContent getContent();

}
