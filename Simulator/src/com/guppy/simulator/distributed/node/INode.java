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
package com.guppy.simulator.distributed.node;

import com.guppy.simulator.broadcast.message.IMessage;
import com.guppy.simulator.common.typdef.NodeId;

/**
 * Represents a node in the network simulation which can interact with other nodes.
 * This interface defines the core behaviors that a node should have, including sending messages,
 * stopping its operation, and handling various types of attacks or interruptions.
 * 
 * <p>Implementations of this interface should ensure thread-safe interactions as 
 * it extends {@code Runnable}, indicating potential multithreaded usage.</p>
 *
 * @see IMessage
 * @see NodeId
 */
public interface INode extends Runnable {

    /**
     * Sends a message to other nodes in the network.
     *
     * @param msg the message to be published
     * @throws InterruptedException if the current thread was interrupted
     */
    void publishMessage(IMessage msg) throws InterruptedException;

    /**
     * Stops the node's operation gracefully.
     */
    void stop();

    /**
     * Retrieves the unique identifier of the node.
     *
     * @return the node's unique ID
     */
    NodeId getNodeId();

    /**
     * Checks if the node is a leader in the network.
     *
     * @return {@code true} if the node is a leader, {@code false} otherwise
     */
    boolean isLeader();

    /**
     * Sets the interruption status for the node.
     * 
     * @param b {@code true} to indicate that the node should be interrupted, {@code false} otherwise
     */
    void setInterrupt(boolean b);

    /**
     * Determines whether to inject a flooding attack to the node.
     * 
     * @param b {@code true} to enable the flooding attack, {@code false} to disable it
     */
    void injectFlooding(boolean b);

    /**
     * Determines whether to make the node drop messages.
     *
     * @param b {@code true} to enable message dropping, {@code false} to disable it
     */
    void injectDropMessage(boolean b);

    /**
     * Determines whether to tamper with the node's messages.
     *
     * @param b {@code true} to enable message tampering, {@code false} to disable it
     */
    void injectMessageTampering(boolean b);
}




