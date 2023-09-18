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
package com.guppy.simulator.broadcast.strategy;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.MessageContent;
import com.guppy.simulator.producermq.KafkaMessageProducer;

/**
 * Represents the contract for various broadcasting strategies.
 * 
 * Implementations should provide concrete broadcasting logic based on different
 * strategies.
 * 
 * @author HegdeSagar
 */
public interface IBroadcastStrategy {

	/**
     * Executes the broadcast strategy with the given message and latency.
     *
     * @param message The message to be broadcasted.
     * @param latency The latency for broadcasting.
     * @return true if the execution was successful, false otherwise.
     * @throws Exception if there's any issue in executing the strategy.
     */
	boolean executeStrategy(IMessage message, long latency) throws Exception;

	/**
     * Broadcasts a message with the leader's content.
     * 
     * @param content The content to be broadcasted.
     * @return true if the broadcast was successful, false otherwise.
     */
	boolean leaderBroadcast(MessageContent content);

	/**
     * Checks if the message has been delivered.
     * 
     * @return true if the message is delivered, false otherwise.
     */
	boolean isDelivered();

	/**
     * Closes Kafka resources associated with the broadcast strategy.
     */
	void close();

	/**
     * Resets the state of the broadcast strategy.
     */
	void reset();

	/**
     * Initiates a flood broadcast.
     */
	void flood();

	/**
     * Initiates dropping of the message.
     */
	void startDropping();

	/**
     * Initiates tampering of the message.
     */
	void startMessageTamper();

	/**
     * Publishes messages that are not delivered.
     * 
     * @param message The message that was not delivered.
     */
	void publishNotDelivered(IMessage message);

	/**
     * Sets the Kafka message producer for the strategy.
     * 
     * @param producer The Kafka message producer to be set.
     */
	void setMQProducer(KafkaMessageProducer producer);

}
