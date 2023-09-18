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

import java.util.concurrent.atomic.AtomicLong;

import com.guppy.simulator.common.typdef.NodeId;
import com.guppy.simulator.producermq.KafkaMessageProducer;

/**
 * Represents an abstract base class for different broadcast strategies.
 * This class provides basic functionalities and fields commonly required 
 * by its subclasses.
 */
public abstract class AbstractBroadcastStrategy {

    /** Number of nodes in the network. */
    protected int N;

    /** Maximum number of faulty nodes allowed in the network. */
    protected int F;

    /** The node ID associated with the broadcast strategy instance. */
    protected NodeId nodeId;

    /** The Kafka message producer responsible for producing messages. */
    protected KafkaMessageProducer producer;

    /** Flag indicating if a message might not be sent. */
    protected boolean maybeDontSendMessage = false;

    /** Flag indicating if redundant messages might be sent. */
    protected boolean maybeSendRedundantMessages = false;

    /** Flag indicating if the content of a message might be altered. */
    protected boolean maybeAlterMessageContent = false;

    /**
     * Constructor to initialize the abstract broadcast strategy with 
     * specified parameters.
     * 
     * @param _N      the number of nodes
     * @param _f      the number of faulty nodes
     * @param nodeId  the node ID
     */
    AbstractBroadcastStrategy(int _N, int _f, NodeId nodeId){
        this.N = _N;
        this.F = _f;
        this.nodeId = nodeId;
    }

    /**
     * Generates a unique message ID.
     * 
     * @return the generated message ID as an AtomicLong
     */
    protected AtomicLong generateIteration() {
        AtomicLong idCounter = new AtomicLong();
        return idCounter;
    }

    /**
     * Closes the Kafka producer.
     */
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

    /**
     * Sets the Kafka message producer.
     * 
     * @param mqProducer  the Kafka message producer
     */
    public void setMQProducer(KafkaMessageProducer mqProducer) {
        this.producer = mqProducer;
    }

    /**
     * Enables the flooding of redundant messages.
     */
    public void flood() {
        this.maybeSendRedundantMessages = true;
    }

    /**
     * Begins the process of dropping messages.
     */
    public void startDropping() {
        this.maybeDontSendMessage = true;
    }

    /**
     * Begins tampering the content of messages.
     */
    public void startMessageTamper() {
        this.maybeAlterMessageContent = true;
    }
}

