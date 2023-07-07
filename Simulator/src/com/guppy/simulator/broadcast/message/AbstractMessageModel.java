package com.guppy.simulator.broadcast.message;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.MessageId;

/**
 * 
 * Represents an abstract message model that provides a base implementation for
 * messages in the broadcast system.
 * 
 * @author HegdeSagar
 */
public abstract class AbstractMessageModel implements IMessage {

	protected enum MessageType {
		SEND, ECHO
	};

	protected MessageId messageId;

	protected MessageContent messageContent;

	protected MessageType type;

	protected LocalDateTime timeStamp;

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

	/**
	 * Generates a unique message ID.
	 * 
	 * @return the generated message ID
	 */
	protected MessageId generateMessageId() {

		AtomicLong idCounter = new AtomicLong();

		String idVal = String.valueOf(idCounter.getAndIncrement());

		return new MessageId(Constants.MESSAGE_ID_PREFIX.concat(idVal));

	}


}
