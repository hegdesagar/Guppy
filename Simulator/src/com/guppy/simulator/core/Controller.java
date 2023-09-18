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
package com.guppy.simulator.core;

import java.util.concurrent.CountDownLatch;

/**
 * Controller class to manage and control the reset mechanism for nodes.
 * It leverages the CountDownLatch to synchronize the reset operations
 * among multiple nodes.
 * 
 * @author HegdeSagar
 */
public class Controller {

    /** A flag to indicate if reset operation is in progress. */
    private volatile boolean resetFlag = false;

    /** Latch to synchronize reset operations among nodes. */
    private CountDownLatch latch;

    /**
     * Initiates the reset operation by setting the resetFlag and initializing the latch.
     *
     * @param numberOfNodes The total number of nodes involved in the reset operation.
     */
    public synchronized void initiateReset(int numberOfNodes) {
        this.resetFlag = true;
        this.latch = new CountDownLatch(numberOfNodes);
    }

    /**
     * Checks if the reset operation is currently in progress.
     *
     * @return true if reset is in progress, false otherwise.
     */
    public synchronized boolean isResetInProgress() {
        return resetFlag;
    }

    /**
     * Decrements the latch count, indicating a node has completed its reset operation.
     */
    public synchronized void decrementLatch() {
        if (latch != null) {
            latch.countDown();
        }
    }

    /**
     * Blocks the current thread until all nodes have completed their reset operation,
     * unless the thread is interrupted.
     */
    public void awaitUntillLatch() {
        try {
            if (latch != null) {
                latch.await();
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status of the thread
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Blocks the current thread until all nodes have completed their reset operation.
     * After all nodes are reset, it will reset the Controller's state.
     */
    public void awaitLatchAndReset() {
        try {
            if (latch != null) {
                latch.await();
            }
            synchronized (this) {
                resetFlag = false;
                latch = null; // Optionally set latch to null after reset
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status of the thread
            Thread.currentThread().interrupt();
        }
    }
}

