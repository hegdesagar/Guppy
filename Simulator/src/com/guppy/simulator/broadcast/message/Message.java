package com.guppy.simulator.broadcast.message;

import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.MessageId;
import com.guppy.simulator.common.typdef.NodeId;

public class Message extends AbstractMessageModel {

	/*public Message(NodeId nodeId, MessageContent msgContent, MessageType messageType) {
		super(msgContent, messageType);

	}*/

	public Message(NodeId nodeId, MessageContent msgContent, MessageType messageType,AtomicLong iteration) {
		super(msgContent, messageType,iteration);
		this.senderId = nodeId;
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

	@Override
	public NodeId getSenderId() {
		return this.senderId;
	}

	@Override
	public MessageContent getContent() {
		return this.messageContent;
	}

	@Override
	public MessageId getMessageId() {
		return messageId;
	}

}
