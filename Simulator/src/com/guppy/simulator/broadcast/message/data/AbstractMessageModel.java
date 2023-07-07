package com.guppy.simulator.broadcast.message.data;

import java.time.LocalDateTime;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.MessageId;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * 
 * Represents an abstract message model that provides a base implementation for
 * messages in the broadcast system.
 * 
 * @author HegdeSagar
 */
public abstract class AbstractMessageModel implements IMessage {

	public enum MessageType {
		SEND, ECHO
	};

	protected MessageId messageId;

	protected MessageContent messageContent;

	protected MessageType type;

	protected LocalDateTime timeStamp;
	
	protected NodeId SenderId;

	/**
	 * Constructor for creating an instance of AbstractMessageModel.
	 *
	 * @param msgContent  The content of the message.
	 * @param messageType The type of the message.
	 */
	public AbstractMessageModel(MessageContent msgContent, MessageType messageType) {

		// The time-stamp representing the current date and time.
		this.timeStamp = LocalDateTime.now();
		// The unique identifier for the message.
		this.messageId = generateMessageId();
		// The content of the message.
		this.messageContent = msgContent;
		// The type of the message.
		this.type = messageType;

	}
	
	public MessageType getType() {
		return this.type;
	}
	

	public abstract MessageContent getContent();
	
	protected abstract MessageId generateMessageId();
	
	public abstract NodeId getSenderId();
		

}
