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

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.broadcast.message.data.AbstractMessageModel.MessageType;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.common.typdef.MessageId;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * Represents a message within the network with essential metadata and content.
 */
public interface IMessage {

	/**
     * Gets the type of the message.
     * 
     * @return The type of the message.
     */
	public MessageType getType();
	
	/**
     * Sets the type of the message.
     * 
     * @param type The type to set for the message.
     */
	public void setType(MessageType type);
	/**
     * Gets the ID of the sender node.
     * 
     * @return The sender node's ID.
     */
	public NodeId getSenderId();
	 /**
     * Gets the content of the message.
     * 
     * @return The content of the message.
     */
	public MessageContent getContent();
	/**
     * Gets the iteration of the message, typically used in scenarios 
     * where the same message type can be sent multiple times.
     * 
     * @return The iteration of the message.
     */
	public AtomicLong getIteration();
	/**
     * Sets the iteration for the message.
     * 
     * @param iteration The iteration to set.
     */
	public void setIteration(AtomicLong iteration);
	/**
     * Gets the timestamp indicating when the message was created or sent.
     * 
     * @return The timestamp of the message.
     */
	public LocalDateTime getTimeStamp();
	/**
     * Sets the timestamp for the message.
     * 
     * @param timeStamp The timestamp to set.
     */
	public void setTimeStamp(LocalDateTime timeStamp);
	/**
     * Gets the unique ID of the message.
     * 
     * @return The unique message ID.
     */
	public MessageId getMessageId();

}
