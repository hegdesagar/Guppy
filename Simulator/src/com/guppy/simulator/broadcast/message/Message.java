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
package com.guppy.simulator.broadcast.message;

import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel;
import com.guppy.simulator.common.Constants;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.MessageId;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * Represents a concrete implementation of a message, extending the functionalities provided by the AbstractMessageModel.
 * This class is responsible for holding the details of the message, its sender, and content.
 * 
 * @author HegdeSagar
 */
public class Message extends AbstractMessageModel {

	/**
     * Constructs a new Message instance.
     *
     * @param nodeId       The ID of the sender node.
     * @param msgContent   The content of the message.
     * @param messageType  The type of the message.
     * @param iteration    The iteration of the message.
     */
	public Message(NodeId nodeId, MessageContent msgContent, MessageType messageType,AtomicLong iteration) {
		super(msgContent, messageType,iteration);
		this.senderId = nodeId;
	}

	/**
     * Generates a unique message ID, prefixed by a constant.
     *
     * @return The generated unique message ID.
     */
	protected MessageId generateMessageId() {

		AtomicLong idCounter = new AtomicLong();

		String idVal = String.valueOf(idCounter.getAndIncrement());

		return new MessageId(Constants.MESSAGE_ID_PREFIX.concat(idVal));

	}

	/**
     * Retrieves the sender ID of the message.
     *
     * @return The ID of the sender node.
     */
	@Override
	public NodeId getSenderId() {
		return this.senderId;
	}

	 /**
     * Retrieves the content of the message.
     *
     * @return The content of the message.
     */
	@Override
	public MessageContent getContent() {
		return this.messageContent;
	}

	 /**
     * Retrieves the unique ID of the message.
     *
     * @return The unique ID of the message.
     */
	@Override
	public MessageId getMessageId() {
		return messageId;
	}

}
