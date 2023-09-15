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
package com.guppy.simulator.broadcast.message.data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

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
		SEND, ECHO , DELIVERED, NOTDELIVERED,INIT,READY
	};
	
	protected MessageId messageId;

	protected MessageContent messageContent;

	protected MessageType type;

	protected LocalDateTime timeStamp;
	
	protected NodeId senderId;

	protected AtomicLong iteration;
	
	/**
	 * Constructor for creating an instance of AbstractMessageModel.
	 *
	 * @param msgContent  The content of the message.
	 * @param messageType The type of the message.
	 * @param iteration 
	 */
	public AbstractMessageModel(MessageContent msgContent, MessageType messageType, AtomicLong iteration) {

		// The time-stamp representing the current date and time.
		this.timeStamp = LocalDateTime.now();
		// The unique identifier for the message.
		this.messageId = generateMessageId();
		// The content of the message.
		this.messageContent = msgContent;
		// The type of the message.
		this.type = messageType;
		// The simulation iteration number
		this.iteration = iteration;

	}
	
	public abstract MessageContent getContent();
	
	protected abstract MessageId generateMessageId();
	
	public abstract NodeId getSenderId();
	
	public MessageType getType() {
		return this.type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}
	
	public AtomicLong getIteration() {
		return iteration;
	}

	public void setIteration(AtomicLong iteration) {
		this.iteration = iteration;
	}
	
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
		
}
